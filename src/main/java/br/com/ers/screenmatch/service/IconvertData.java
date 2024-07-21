package br.com.ers.screenmatch.service;

public interface IconvertData {

    <T> T obtainData (String json, Class<T> classe);
}
