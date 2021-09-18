package com.example.gl.response;

import com.example.gl.model.PublisherModel;

import java.util.ArrayList;
import java.util.List;

public class ResponsePublisherModel extends Responses
{

    private List<PublisherModel> results = new ArrayList<>();

    public List<PublisherModel> getResults() { return results; }

    public void setResults(List<PublisherModel> results) { this.results.addAll(results); }

    @Override
    public Integer getCount() {
        return super.getCount();
    }

    @Override
    public void setCount(Integer count) {
        super.setCount(count);
    }

    @Override
    public String getNext() {
        return super.getNext();
    }

    @Override
    public void setNext(String next) {
        super.setNext(next);
    }

    @Override
    public String getPrevious() {
        return super.getPrevious();
    }

    @Override
    public void setPrevious(String previous) {
        super.setPrevious(previous);
    }
}
