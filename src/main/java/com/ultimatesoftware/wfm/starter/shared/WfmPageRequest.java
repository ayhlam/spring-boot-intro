package com.ultimatesoftware.wfm.starter.shared;

import org.springframework.data.domain.PageRequest;

public class WfmPageRequest extends PageRequest {

    private static final long serialVersionUID = 1L;

    public WfmPageRequest(int page, int perPage) {
        super(page - 1, perPage);
    }

}
