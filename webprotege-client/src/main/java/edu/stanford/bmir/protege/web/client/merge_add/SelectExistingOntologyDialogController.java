package edu.stanford.bmir.protege.web.client.merge_add;

import edu.stanford.bmir.protege.web.client.Messages;

import com.google.gwt.core.client.GWT;

import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeOKCancelDialogController;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;

public class SelectExistingOntologyDialogController extends WebProtegeOKCancelDialogController<OWLOntologyID> {

    private static final Messages MESSAGES = GWT.create(Messages.class);
    final private SelectExistingOntologyView view;

    public SelectExistingOntologyDialogController(SelectExistingOntologyView view) {
        super(MESSAGES.merge_selectOntology());
        this.view = view;
    }

    @Override
    public Widget getWidget() {
        return view.asWidget();
    }

    @Nonnull
    @Override
    public java.util.Optional<HasRequestFocus> getInitialFocusable() {
        return view.getInitialFocusable();
    }

    @Override
    public OWLOntologyID getData(){
        return view.getOntology();
    }
}
