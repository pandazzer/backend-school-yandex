package com.example.backendschoolyandex.Services;

import com.example.backendschoolyandex.Json.AVGPriceJson;

public class HelpClass{
    AVGPriceJson stringJson;
    int sumItems;
    int countItems;

    public HelpClass(AVGPriceJson stringJson, int sumItems, int countItems) {
        this.stringJson = stringJson;
        this.sumItems = sumItems;
        this.countItems = countItems;
    }

    public AVGPriceJson getStringJson() {
        return stringJson;
    }
}
