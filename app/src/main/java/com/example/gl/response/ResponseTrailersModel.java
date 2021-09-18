package com.example.gl.response;

import com.example.gl.model.TrailersModel;

import java.util.ArrayList;
import java.util.List;

public class ResponseTrailersModel extends Responses
{

    private List<TrailersModel> results = new ArrayList<>();

    public List<TrailersModel> getResults() {
        return results;
    }

    public void setResults(List<TrailersModel> results) {
        this.results.addAll(results);
    }

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
