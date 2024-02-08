package sellyourunhappiness.core.config.page;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageModel<T> {

    private final int totalPage;
    private final int pageSize;
    private final int number;
    private final long totalElements;
    private final List<T> contents;

    public PageModel(Page<T> page) {
        this.totalPage = page.getTotalPages();
        this.pageSize = page.getSize();
        this.number = page.getNumber();
        this.totalElements = page.getTotalElements();
        this.contents = page.getContent();
    }
}


