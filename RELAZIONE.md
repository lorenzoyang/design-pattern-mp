# Relazione Progetto: Free Media Platform

Studente: Lorenzo Yang

Matricola: 7136074

Corso: Metodologie di Programmazione

## Descrizione delle funzionalità del sistema implementato

Il progetto consiste nella simulazione di una piattaforma gratuita di streaming multimediale che consente agli utenti di visualizzare e guardare contenuti multimediali. Per semplificare il progetto e mantenere la coerenza con la sua natura gratuita, non sono state implementate funzionalità come registrazione e login: i contenuti sono accessibili liberamente, senza autenticazione. I componenti principali del sistema sono:

- `Content`: rappresenta i contenuti multimediali disponibili sulla piattaforma, suddivisi in:
  - `Movie`: un film composto da un unico `Episode`. Inizialmente avevo pensato di aggiungere anche una classe `Video` che rappresentava il file multimediale del video stesso, ma ho poi evitato questo ulteriore livello di astrazione in quanto non strettamente rilevante per gli obiettivi del corso. Come nella realtà, i film non prevedono stagioni, quindi il concetto di stagione è stato omesso.
  - `TVSeries`: una serie TV strutturata in più `Season`, ognuna contenente diversi `Episode`. Anche qui inizialmente avevo pensato di realizzare il concetto di `Season` con una semplice lista di `Episode`, ma considerando che una serie TV può avere anche più stagioni, e l'uso di liste di `Episode` rendeva meno leggibile e chiaro il codice (violando anche i principi di design), ho quindi deciso di implementare il concetto di `Season` come una classe a parte.
  - **Criteri di uguaglianza tra contenuti:**: in questo progetto, **due contenuti sono considerati uguali se hanno lo stesso titolo, indipendentemente dal fatto che siano film o serie TV.** Ad esempio, un `Movie` e una `TVSeries` con lo stesso titolo sono considerati equivalenti. (Ho eliminato il controllo di `Class` nel metodo `equals`, ma per essere uguali entrambi devono comunque essere sottoclassi di `Content`). 
- `PlatformUser`: rappresenta un utente della piattaforma. In una prima versione, avevo previsto funzionalità più complesse come registrazione, gestione dei preferiti e cronologia di visione, ma ho poi optato per un approccio più semplice. L’utente ha ora un ruolo essenziale come "osservatore" del sistema: solo chi si registra (tramite email) riceve notifiche quando vengono aggiunti o aggiornati contenuti. L’email non è modificabile dopo la registrazione; per cambiarla, è sufficiente eliminare l’utente e registrarne uno nuovo.
- `StreamingPlatform`: rappresenta la piattaforma stessa, che gestisce contenuti e utenti (registrati come osservatori). È il componente principale che coordina l’interazione tra contenuti, utenti e funzionalità offerte. Di seguito chiarisco meglio alcune funzionalità principali della piattaforma:
  - **Funzionalità di aggiornamento dei contenuti (`updateContent`):** trova il contenuto da aggiornare (secondo i criteri di uguaglianza definiti) e lo aggiorna (sostituisce) con il nuovo contenuto fornito (deve avere quinidi lo stesso titolo). Come conseguenza, posso quindi aggiornare un film con una serie TV o viceversa, ma il titolo deve essere lo stesso.
  - **Funzionalità di visione dei contenuti (`watchContent`):** è una simulazione della visione di un contenuto, restituisce una lista di episodi in ordine di visione, per i film restituisce una lista con un solo episodio, per le serie TV restituisce una lista di episodi ordinati per stagione e numero di episodio.

## Design pattern applicati

- Builder
- Visitor
- Observer
- Adapter (versione "degenere")

## Scelte di design e dettagli implementativi

### Builder

Per la creazione degli oggetti `Content` (`Movie` e `TVSeries`), che sono complessi e con diversi campi opzionali, ho utilizzato il pattern Builder (basato su fluent interface). L'intento di questo pattern è di separare la costruzione di un oggetto complesso dalla sua rappresentazione. Ho creato anche una superclasse astratta `ContentBuilder` per evitare la duplicazione di codice nei builder specifici (`MovieBuilder` e `TVSeriesBuilder`): `Movie` e `TVSeries` essendo derivati dalla stessa superclasse `Content`, hanno diversi campi in comune come `title`, `description`, ..., senza un Builder in comune avrei dovuto riscrivere gli stessi metodi in entrambi i builder, per i campi in comune, come `withDescription`, `withReleaseDate`, `withResolution`, ecc.

```java
// ContentBuilder
public ContentBuilder withDescription(String description) { 
  // ...
  return this;
}
```
Tuttavia, questo approccio non risolve del tutto la duplicazione, perché sono comunque costretto a riscrivere i metodi ereditati per modificare il tipo di ritorno. Infatti, per esempio se `withDescription` restituisce un `ContentBuilder`, non posso concatenare metodi specifici di `TVSeriesBuilder` come `withSeason`. Riscrivere i metodi solo per adeguare il tipo di ritorno rende superfluo l’uso di una superclasse. Per ovviare a questo, ho adottato l’uso dei generics, definendo la superclasse come segue:

```java
ContentBuilder<T>
// e per restringere ulteriormente il tipo:
// nel nostro caso T può essere solamente MovieBuilder o TVSeriesBuilder
ContentBuilder<T extends ContentBuilder<T>>
```
Con questa soluzione, però, non è possibile restituire direttamente `this`, perché il compilatore riconosce `this` solo come un `ContentBuilder<T>` e non come il tipo specifico `T`. Per risolvere, ho introdotto un metodo astratto `self()` con tipo di ritorno `T`, che viene implementato nelle sottoclassi e restituito al posto di this, garantendo così la corretta inferenza del tipo da parte del compilatore (il tipo che restituisce `self()` e quello dichiarato nei metodi del `ContentBuilder` combaciano).

Le classi `MovieBuilder` e `TVSeriesBuilder` includono numerosi controlli per assicurarsi che tutti i campi siano impostati correttamente. Con l'uso del Builder, tali verifiche avvengono al momento della costruzione dell’oggetto: con i metodi con prefisso 'with' in questo caso, una volta chiamato il metodo `build()`, possiamo essere certi che l’oggetto risultante sia valido.

Per mantenere questa garanzia anche nel tempo, ho deciso di rendere immutabili tutte le classi coinvolte (`Movie`, `TVSeries`, ma anche `Season` ed `Episode`). In questo modo, una volta costruito un oggetto, non potrà più essere modificato. Ciò semplifica la condivisione degli oggetti e previene comportamenti imprevisti dovuti a modifiche non controllate.

**Testing:** poiché `Content` e `ContentBuilder` sono classi astratte,  i metodi come `withDescription`, `withReleaseDate`, ecc. non sono direttamente testabili, li ho quindi testati rispettivamente nei test di `Movie`, e `TVSeries`, che estendono `Content`, le classi `MovieBuilder` e `TVSeriesBuilder` che sono inner class di `Movie` e `TVSeries`, non li ho testati separatamente in un test file dedicato, ma direttamente nei test di `Movie` e `TVSeries`, durante la costruzione degli oggetti, in questo nonostante io abbia avuto un po' di duplicazione di codice nel testare i metodi in comune come `withDescription`, `withReleaseDate`, ecc., ma tali duplicazioni sono accettabili in quanto i metodi sono semplici e rende anche più chiaro il test, visto che i metodi sono specifici per ogni tipo di contenuto. Essendoci tanti controlli da testare, nei test di `Movie` e `TVSeries` per la loro costruzione ho testato prima la "situazione felice" e poi quelle in cui si dovrebbe verificare un errore.

### Visitor

Il pattern Visitor è stato applicato in due contesti distinti per aggiungere nuove operazioni a gerarchie di classi esistenti senza modificarle, rispettando i principi Open-Closed (OCP) e Single Responsibility (SRP).

La gerarchia `Content` potrebbe aver bisogno, in futuro, di nuove funzionalità, come ad esempio esportare i contenuti in diversi formati oppure calcolare delle statistiche. Aggiungere questi metodi direttamente dentro le classi `Movie` e `TVSeries` non sarebbe una buona scelta, perché renderebbe il codice più complicato e meno leggibile. Inoltre, ogni volta che servirebbe una nuova funzionalità, dovrei andare a modificare le classi già esistenti cosa che si cerca di evitare. Usando il pattern Visitor, invece, posso aggiungere nuove operazioni senza toccare le classi della gerarchia `Content`.

Anche nella gerarchia degli eventi (`PlatformEvent`, `AddContentEvent`, `UpdateContentEvent`, `RemoveContentEvent`) ho utilizzato il Visitor, ma nel contesto del pattern Observer. Serve perché gli osservatori devono reagire in modo diverso a seconda del tipo di evento ricevuto. Per esempio, un `AddContentEvent` contiene solo il nuovo contenuto da aggiungere, mentre un `UpdateContentEvent` contiene sia la versione vecchia che quella aggiornata del contenuto.

**Testing:** per la gerarchia `Content`, ho creato test file per ogni implementazione concreta del Visitor, come ad esempio `DisplayContentVisitor`, i contenuti multimediali creati per testare i Visitor concreti, sono sempre trattati come `Content` solo così si sfrutta il double-dispatch usando due volte il binding dinamico. Nella gerarchia degli eventi, invece gli visitori concreti sono stati implementati e istanziati tramite classi anonime, quindi creati al volo nei metodi degli observer, le loro funzionalità sono state testate quindi direttamente nei test file degli osservatori (`PlatformUserTest` e `PlatformEventLoggerTest`).

### Observer

Per gestire le notifiche relative ai cambiamenti dei contenuti sulla piattaforma, ho implementato il pattern Observer. Questo pattern consente di notificare gli interessati (osservatori) quando si verificano eventi significativi, come l'aggiunta, l'aggiornamento o la rimozione di contenuti. In questo modo, gli utenti registrati e i logger (nel mio caso) possono essere informati in modo automatico e reattivo, senza dover controllare continuamente lo stato della piattaforma. Nel mio caso avendo un solo tipo di soggetto concreto non ho implementato un AbstractSubject, la gestione degli osservatori li ho implementati direttamente nella `StreamingPlatform` stessa.

**Testing:** la logica di gestione degli osservatori (registrazione, rimozione, notifica) è stata testata in `StreamingPlatformTest` nella quale per i `PlatformObserver` ho creato al volo una sua mock tramite lambda expression. Gli osservatori concreti (`PlatformUser`, `PlatformEventLogger`) sono stati testati invece in file separati, dove è stata creata un'istanza di `StreamingPlatform` per agire da soggetto da osservare, ho dovuto quindi testare prima il soggetto `StreamingPlatform` e poi gli osservatori concreti. Per `PlatformUser`, ho avuto bisogno di creare un mock per il servizio email usato per simulare l'invio di notifiche agli utenti, il mock contiene un campo di dati per salvare l'ultima notifica inviata per facilitare i test.


### Adapter

Nell'implementazione dell'Observer, ho notato che alcuni osservatori potrebbero non essere interessati a tutti i tipi di eventi. Ad esempio, `PlatformUser` reagisce solo all'aggiunta e all'aggiornamento di contenuti, ignorando la rimozione. Per evitare di dover implementare metodi vuoti nell'osservatore per gli eventi non pertinenti, ho utilizzato la variante del pattern Adapter che fornisce un'implementazione di default. Invece di implementare direttamente `PlatformEventVisitor`, i miei osservatori concreti usano una classe anonima che estende PlatformEventVisitorAdapter, sovrascrivendo solo i metodi di loro interesse. `PlatformUser`, ad esempio, ridefinisce solo `visitAddContent` e `visitUpdateContent`. Quindi solo i clienti che sono interessati a gestire tutti gli eventi della piattaforma devono implementare l'interfaccia `PlatformEventVisitor`, mentre gli altri possono semplicemente estendere l'adapter e ridefinire solo i metodi che gli interessano, questa soluzioen quindi dà più libertà di scelta ai clienti rispetto "default method" nelle interfacce introdotte in Java 8.

**Testing:** La classe `PlatformEventVisitorAdapter` è astratta, viene quindi testata in modo indiretto nei test degli osservatori concreti (in questo caso `PlatformUser`) (tramite `testNotifyChangeForRemoveContentEvent`).


### Descrizione dei partecipanti dei pattern

#### Builder

- `ContentBuilder`: la classe astratta che definisce i metodi comuni per costruire un contenuto (film o serie TV) 
- `MovieBuilder`: la classe concreta che estende `ContentBuilder` per costruire oggetti oggetti della classe `Movie`
- `TVSeriesBuilder`: la classe concreta che estende `ContentBuilder` per costruire oggetti della classe `TVSeries`

#### Visitor

- per la gerarchia `Content`:
  - `Content`: la classe astratta che rappresenta i contenuti multimediali, contiene il metodo astratto `accept` per accettare un visitor
    - `Movie`: la classe concreta (il 'contenuto' concreto) che rappresenta un film
    - `TVSeries`: la classe concreta (il 'contenuto' concreto) che rappresenta una serie TV
  - `ContentVisitor`: l'interfaccia che definisce i metodi per visitare i contenuti
  - `DisplayContentVisitor`: l'implementazione concreta del visitor per implementare la visualizzazione dei contenuti
  - `PlaybackContentVisitor`: l'implementazione concreta del visitor per implementare la visione dei contenuti (simulazione della visione)
- per la gerarchia degli eventi:
  - `PlatformEvent`: l'interfaccia che rappresenta un evento della piattaforma, contiene il metodo `accept` per accettare un visitor
    - `AddContentEvent`: la classe concreta (l'evento concreto) che rappresenta l'aggiunta di un nuovo contenuto
    - `UpdateContentEvent`: la classe concreta (l'evento concreto) che rappresenta l'aggiornamento di un contenuto esistente
    - `RemoveContentEvent`: la classe concreta (l'evento concreto) che rappresenta la rimozione di un contenuto
  - `PlatformEventVisitor`: l'interfaccia che definisce i metodi per visitare gli eventi della piattaforma
  - Un'istanza della classe anonima che estende `PlatformEventVisitorAdapter` (nel caso di `PlatformUser`)
  - Un'istanza della classe anonima che implementa `PlatformEventVisitor` (nel caso di `PlatformEventLogger`)

#### Observer

- `StreamingPlatform`: la classe concreta che funge da soggetto osservabile, gestisce gli osservatori e notifica gli eventi
- `PlatformObserver`: l'interfaccia che definisce i metodi per ricevere notifiche sugli eventi della piattaforma, l'intefaccia che gli osservatori devono implementare.
- `PlatformEventLogger`: l'osservatore concreto che registra gli eventi della piattaforma (compie operazioni di logging)
- `PlatformUser`: l'osservatore concreto che riceve notifiche sugli eventi della piattaforma e agisce di conseguenza (in questo caso, notifica gli utenti nel mondo reale tramite servizi email)

### UML

