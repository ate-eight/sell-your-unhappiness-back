package sellyourunhappiness.api.config.page;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PageValue {
    private final int totalPage;
    private final int pageSize;
    private final int currentPage;
    private final long totalElements;
    private final long num;
    private final List<Integer> block;
    private Integer previous;
    private Integer after;

    public PageValue(int totalPage, int pageSize, int number, long totalElements) {
        this.totalPage = totalPage;
        this.currentPage = setCurrentPage(number);
        this.totalElements = totalElements;
        this.pageSize = pageSize;
        this.num = setNum();
        this.block = setBlock();
    }

    private List<Integer> setBlock() {
        List<Integer> result = new ArrayList<>();

        int startNum = setStartNum();
        int endNum = setEndNum(startNum);

        setPrevious(startNum);
        setAfter(endNum);

        for (int i = startNum; i <= endNum; i++) {
            result.add(i);
        }

        return result;
    }

    private void setPrevious(int num) {
        if (num != 1) {
            this.previous = num - 10;
        }
    }

    private void setAfter(int num) {
        if (num < this.totalPage) {
            this.after = num + 1;
        }
    }

    private int setStartNum() {
        return ((int) Math.floor((double) (this.currentPage - 1) / 10) * 10 + 1);
    }

    private int setEndNum(int startNum) {
        return Math.min(startNum + (this.pageSize - 1), this.totalPage);
    }

    private int setCurrentPage(int num) {
        return num + 1;
    }

    private long setNum() {
        return this.totalElements - ((long) this.pageSize * (this.currentPage - 1));
    }

}


