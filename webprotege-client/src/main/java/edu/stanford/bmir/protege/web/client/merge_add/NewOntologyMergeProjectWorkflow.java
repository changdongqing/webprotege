package edu.stanford.bmir.protege.web.client.merge_add;

import edu.stanford.bmir.protege.web.client.Messages;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallbackWithProgressDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.ProgressDisplay;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.merge_add.NewOntologyMergeAddAction;
import edu.stanford.bmir.protege.web.shared.merge_add.NewOntologyMergeAddResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

public class NewOntologyMergeProjectWorkflow {
    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final MessageBox messageBox;

    @Nonnull
    private final DispatchErrorMessageDisplay errorDisplay;

    @Nonnull
    private final ProgressDisplay progressDisplay;

    private final Messages messages;

    @Inject
    public NewOntologyMergeProjectWorkflow(@Nonnull DispatchServiceManager dispatchServiceManager,
                                           @Nonnull MessageBox messageBox,
                                           Messages messages,
                                           @Nonnull DispatchErrorMessageDisplay errorDisplay,
                                           @Nonnull ProgressDisplay progressDisplay) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.messageBox = messageBox;
        this.messages = messages;
        this.errorDisplay = errorDisplay;
        this.progressDisplay = progressDisplay;
    }

    public void start(ProjectId projectId, DocumentId documentId, List<OWLOntologyID> ontologyList){
        getIRI(projectId, documentId, ontologyList);
    }

    private void getIRI(ProjectId projectId, DocumentId documentId, List<OWLOntologyID> ontologyList){
        EnterOntologyIRIView view = new EnterOntologyIRIViewImpl();
        EnterOntologyIRIDialogController controller = new EnterOntologyIRIDialogController(view);
        controller.setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<String>() {
            @Override
            public void handleHide(String data, WebProtegeDialogCloser closer) {
                mergeAdd(projectId, documentId, view.getOntologyIRI(), ontologyList);
                closer.hide();
            }
        });
        WebProtegeDialog.showDialog(controller);
    }

    private void mergeAdd(ProjectId projectId, DocumentId documentId, String iri, List<OWLOntologyID> ontologyList){
        dispatchServiceManager.execute(new NewOntologyMergeAddAction(projectId, documentId, iri, ontologyList), new DispatchServiceCallbackWithProgressDisplay<NewOntologyMergeAddResult>(errorDisplay, progressDisplay) {
            @Override
            public String getProgressDisplayTitle() {
                return messages.merge_progressUploadingAndMerging();
            }

            @Override
            public String getProgressDisplayMessage() {
                return messages.merge_progressMergingIntoNew();
            }

            @Override
            public void handleSuccess(NewOntologyMergeAddResult result){
                messageBox.showMessage(messages.merge_uploadedMergedSuccessfully());
            }
        });
    }


}
