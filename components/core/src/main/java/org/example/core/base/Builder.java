package org.example.core.base;

import java.io.Serializable;

public interface Builder<T> extends Serializable {
    T builder();
}
