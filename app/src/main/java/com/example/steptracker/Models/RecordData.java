package com.example.steptracker.Models;

public class RecordData {
    private String recordData, recordLabel;

    public RecordData(String recordData, String recordLabel) {
        this.recordData = recordData;
        this.recordLabel = recordLabel;
    }

    public String getRecordData() {
        return recordData;
    }

    public void setRecordData(String recordData) {
        this.recordData = recordData;
    }

    public String getRecordLabel() {
        return recordLabel;
    }

    public void setRecordLabel(String recordLabel) {
        this.recordLabel = recordLabel;
    }
}
