package com.example.bank.common.service;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Subject {

    private final List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public abstract void notifyObservers(Object data);
}