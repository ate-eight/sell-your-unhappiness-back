package sellyourunhappiness.core.config.page;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@NoArgsConstructor
public class PageRequest {

    private final Sort.Direction direction = Sort.Direction.DESC;
    private Integer page;
    private Integer size;
    private String sort = "createTime";

    public PageRequest(Integer page) {
        this.page = setPage(page);
        this.size = 10;
    }

    public PageRequest(Integer page, Integer size) {
        this.page = setPage(page);
        this.size = pageSize(size);
    }

    private static Integer setPage(Integer page) {
        return (page == null || page <= 0) ? 1 : page;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    private int pageSize(int size) {
        int default_size = 10;
        int max_size = 50;
        return size > max_size ? default_size : size;
    }

    public org.springframework.data.domain.PageRequest of() {
        return org.springframework.data.domain.PageRequest.of(page - 1, size, Sort.by(direction, sort));
    }

}


