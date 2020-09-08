package org.seckill.dto;

public class Exposer {

    private boolean expesed;//活动开启标识

    private String md5;//加密MD5

    private long seckillId;//活动ID

    private long now;//当前时间

    private long start;//活动开始时间

    private long end;//活动结束时间

    public Exposer(boolean expesed, String md5, long seckillId) {
        this.expesed = expesed;
        this.md5 = md5;
        this.seckillId = seckillId;
    }

    public Exposer(boolean expesed, long seckillId, long now, long start, long end) {
        this.expesed = expesed;
        this.seckillId = seckillId;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    public Exposer(boolean expesed, long seckillId) {
        this.expesed = expesed;
        this.seckillId = seckillId;
    }

    public Exposer(boolean expesed, long now, long start, long end) {
        this.expesed = expesed;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    public boolean isExpesed() {
        return expesed;
    }

    public void setExpesed(boolean expesed) {
        this.expesed = expesed;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Exposer{" +
                "expesed=" + expesed +
                ", md5='" + md5 + '\'' +
                ", seckillId=" + seckillId +
                ", now=" + now +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
