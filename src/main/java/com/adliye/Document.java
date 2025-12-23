package com.adliye;

import java.sql.Timestamp;

public class Document {

    private int docId;
    private int caseId;
    private String docName;
    private String filepath;
    private Timestamp uploadedAt;

    public int getDocId() { return docId; }
    public void setDocId(int docId) { this.docId = docId; }

    public int getCaseId() { return caseId; }
    public void setCaseId(int caseId) { this.caseId = caseId; }

    public String getDocName() { return docName; }
    public void setDocName(String docName) { this.docName = docName; }

    public String getFilepath() { return filepath; }
    public void setFilepath(String filepath) { this.filepath = filepath; }

    public Timestamp getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(Timestamp uploadedAt) { this.uploadedAt = uploadedAt; }
}
