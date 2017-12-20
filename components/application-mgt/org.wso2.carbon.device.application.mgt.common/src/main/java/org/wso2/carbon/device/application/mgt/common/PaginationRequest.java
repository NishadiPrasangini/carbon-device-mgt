/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * you may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.device.application.mgt.common;

import org.wso2.carbon.device.application.mgt.common.exception.InputValidationException;
import org.wso2.carbon.device.mgt.jaxrs.beans.ErrorResponse.ErrorResponse;

/**
 * This class holds required parameters for a querying a paginated device response.
 */
public class PaginationRequest {

    private int offSet;
    private int limit;

    public PaginationRequest(int start, int limit) {
        this.offSet = start;
        this.limit = limit;
    }

    public int getOffSet() {
        return offSet;
    }

    public void setOffSet(int offSet) {
        this.offSet = offSet;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void validatePaginationRequest(int offSet,int limit){
        if (offSet < 0) {
            throw new InputValidationException(
                    new ErrorResponse.ErrorResponseBuilder().setCode(400l).setMessage("Request parameter offset is s " +
                            "negative value.").build());
        }
        if (limit < 0) {
            throw new InputValidationException(
                    new ErrorResponse.ErrorResponseBuilder().setCode(400l).setMessage("Request parameter limit is a " +
                            "negative value.").build());
        }
        if (limit > 100) {
            throw new InputValidationException(
                    new ErrorResponse.ErrorResponseBuilder().setCode(400l).setMessage("Request parameter limit should" +
                            " be less than or equal to 100.").build());
        }
    }


    @Override
    public String toString() {
        return "Off Set'" + this.offSet + "' limit '" + this.limit;
    }
}
