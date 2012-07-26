package org.codehaus.grails.struts;

import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

/**
 * This class allows for the struts1 plugin to use multipart/form-data requests. Otherwise the default
 * resolver will gobble up the request and your struts multipart forms will be null/empty
 * User: Ryan Vanderwerf
 * Date: Mar 2, 2010
 * Time: 5:04:51 PM
 */
public class StrutsAwareMultipartResolver extends CommonsMultipartResolver  {
     private static final LinkedMultiValueMap<String, MultipartFile> EMPTY_MULTI_VALUE_MAP = new LinkedMultiValueMap<String, MultipartFile>();

	static final Log LOG = LogFactory.getLog(StrutsAwareMultipartResolver.class );
    String strutsActionExtension;

    public String getStrutsActionExtension() {
        return strutsActionExtension;
    }

    public void setStrutsActionExtension(String strutsActionExtension) {
        this.strutsActionExtension = strutsActionExtension;
    }

    protected MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
        try {
            return super.parseRequest(request);
        } catch (MultipartException e) {
            if(e.getCause() != null && e.getCause().getClass().equals(FileUploadBase.UnknownSizeException.class)) {
                LOG.warn(e.getMessage() );
                return new MultipartParsingResult(EMPTY_MULTI_VALUE_MAP, Collections.EMPTY_MAP, Collections.EMPTY_MAP);
            }
            else {
                throw e;
            }
        }
    }

    /**
     * checks URI for struts extension
     * @param request
     * @return
     */
    private boolean isStrutsAction(HttpServletRequest request) {
        String extension = ".do";
        if (strutsActionExtension!=null) {
            extension = getStrutsActionExtension();
        }
        if (request.getRequestURI().endsWith(extension)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isMultipart(HttpServletRequest request) {
        boolean isMultiPart = super.isMultipart(request);
        boolean isStrutsAction = false;
        isStrutsAction = isStrutsAction(request);
        if (isMultiPart && !isStrutsAction) {
            return true;
        } else {
            return false;
        }        
    }
}
