package com.fmi110.bean;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author fmi110
 * @Date 2018/6/4 21:46
 */
public class TextPair implements WritableComparable<TextPair> {

    private TextPair first;
    private TextPair second;

    public TextPair() {
        set(new TextPair(), new TextPair());
    }

    public void set(TextPair first, TextPair second) {
        this.first = first;
        this.second = second;
    }

    public TextPair(TextPair first, TextPair second) {
        this.first = first;
        this.second = second;
    }

    public TextPair getFirst() {
        return first;
    }

    public void setFirst(TextPair first) {
        this.first = first;
    }

    public TextPair getSecond() {
        return second;
    }

    public void setSecond(TextPair second) {
        this.second = second;
    }


    @Override
    public int compareTo(TextPair o) {
        int cmp = first.compareTo(o.getFirst());
        if(cmp == 0){
            return second.compareTo(o.getSecond());
        }
        return cmp;
    }


    @Override
    public void write(DataOutput out) throws IOException {
        first.write(out);
        second.write(out);
    }


    @Override
    public void readFields(DataInput in) throws IOException {
        first.readFields(in);
        second.readFields(in);
    }

    @Override
    public String toString() {
        return "IntPair{" +
               "first=" + first +
               ", second=" + second +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextPair textPair = (TextPair) o;

        if (first != null ? !first.equals(textPair.first) : textPair.first != null) return false;
        return second != null ? second.equals(textPair.second) : textPair.second == null;
    }

    @Override
    public int hashCode() {
        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + (second != null ? second.hashCode() : 0);
        return result;
    }
}
