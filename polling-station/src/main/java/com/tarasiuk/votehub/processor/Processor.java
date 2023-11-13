package com.tarasiuk.votehub.processor;

public interface Processor<ITEM> {

    void process(ITEM vote);

}
