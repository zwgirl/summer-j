package com.formgen.eclipse;

import org.eclipse.wst.common.project.facet.core.IActionConfigFactory;

public final class FormGenCoreFacetInstallConfig
{
    private String urlPattern = "*.form";

    public String getUrlPattern()
    {
        return this.urlPattern;
    }

    public void setUrlPattern( final String urlPattern )
    {
        this.urlPattern = urlPattern;
    }

    public static final class Factory implements IActionConfigFactory
    {
        public Object create()
        {
            return new FormGenCoreFacetInstallConfig();
        }
    }
}
