package sellyourunhappiness.api.config.page;

import lombok.Getter;
import sellyourunhappiness.core.config.page.PageModel;

import java.util.List;

@Getter
public class PageResponse<T>{
    private final PageValue pageValue;
    private final List<T> contents;

    public PageResponse(PageValue pageValue, List<T> contents) {
        this.pageValue = pageValue;
        this.contents = contents;
    }

    public PageResponse(PageModel model, List<T> contents) {
        this.pageValue = new PageValue(model.getTotalPage(), model.getPageSize(), model.getNumber(), model.getTotalElements());
        this.contents = contents;
    }
}


