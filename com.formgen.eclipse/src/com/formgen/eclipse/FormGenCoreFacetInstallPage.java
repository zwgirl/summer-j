package com.formgen.eclipse;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.common.project.facet.ui.AbstractFacetWizardPage;

public final class FormGenCoreFacetInstallPage extends AbstractFacetWizardPage
{
    private FormGenCoreFacetInstallConfig config;
    private Text urlPatternTextField;

    public FormGenCoreFacetInstallPage()
    {
        super( "formgen.core.facet.install.page" );

        setTitle( "FormGen Core" );
        setDescription( "Configure the FormGen servlet." );
    }

    public void createControl( final Composite parent )
    {
        final Composite composite = new Composite( parent, SWT.NONE );
        composite.setLayout( new GridLayout( 1, false ) );

        final Label label = new Label( composite, SWT.NONE );
        label.setLayoutData( gdhfill() );
        label.setText( "URL Pattern:" );

        this.urlPatternTextField = new Text( composite, SWT.BORDER );
        this.urlPatternTextField.setLayoutData( gdhfill() );
        this.urlPatternTextField.setText( this.config.getUrlPattern() );

        setControl( composite );
    }

    public void setConfig( final Object config )
    {
        this.config = (FormGenCoreFacetInstallConfig) config;
    }

    public void transferStateToConfig()
    {
        this.config.setUrlPattern( this.urlPatternTextField.getText() );
    }

    private static GridData gdhfill()
    {
        return new GridData( GridData.FILL_HORIZONTAL );
    }
}
