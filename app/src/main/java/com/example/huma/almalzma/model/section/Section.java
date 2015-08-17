package com.example.huma.almalzma.model.section;

import com.example.huma.almalzma.model.Link;
import com.example.huma.almalzma.model.PDF;

public class Section {
    private Link mLink;
    private PDF mPDF;

    public Link getLink() {
        return mLink;
    }

    public void setLink(Link link) {
        mLink = link;
    }

    public PDF getPDF() {
        return mPDF;
    }

    public void setPDF(PDF PDF) {
        mPDF = PDF;
    }
}
