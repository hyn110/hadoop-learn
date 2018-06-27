package com.fmi110.bean;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author fmi110
 * @Date 2018/6/4 21:46
 */
public class IntPair implements WritableComparable<IntPair> {

    private IntWritable first;
    private IntWritable second;

    public IntPair() {
        set(new IntWritable(0), new IntWritable(0));
    }

    public void set(IntWritable first, IntWritable second) {
        this.first = first;
        this.second = second;
    }

    public IntPair(IntWritable first, IntWritable second) {
        this.first = first;
        this.second = second;
    }

    public IntWritable getFirst() {
        return first;
    }

    public void setFirst(IntWritable first) {
        this.first = first;
    }

    public IntWritable getSecond() {
        return second;
    }

    public void setSecond(IntWritable second) {
        this.second = second;
    }


    @Override
    public int compareTo(IntPair o) {
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
        return first +
               "," + second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntPair intPair = (IntPair) o;

        if (first != null ? !first.equals(intPair.first) : intPair.first != null) return false;
        return second != null ? second.equals(intPair.second) : intPair.second == null;
    }

    @Override
    public int hashCode() {
        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + (second != null ? second.hashCode() : 0);
        return result;
    }
}
