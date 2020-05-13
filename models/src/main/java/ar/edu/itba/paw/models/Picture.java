package ar.edu.itba.paw.models;

import ar.edu.itba.paw.persistenceAnnotations.Column;
import ar.edu.itba.paw.persistenceAnnotations.Table;

@Table(name = "picture", primaryKey = "picture_id")
public class Picture extends GenericModel<Integer> {
    @Column(name = "name")
    private String name;
    @Column(name = "mime_type", required = true)
    private String mimeType;
    @Column(name = "size", required = true)
    private Long size;
    @Column(name = "data", required = true)
    private byte[] data;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Long getSize() {
        return this.size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    protected boolean isSameInstance(Object o) {
        return o instanceof Picture;
    }
}
