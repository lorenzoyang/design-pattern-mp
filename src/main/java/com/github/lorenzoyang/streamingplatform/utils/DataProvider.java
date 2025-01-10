package com.github.lorenzoyang.streamingplatform.utils;

import java.util.List;

public interface DataProvider<T> {
    List<T> fetchData();
}
