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
import edu.stanford.bmir.protege.web.shared.merge_add.ExistingOntologyMergeAddAction;
import edu.stanford.bmir.protege.web.shared.merge_add.ExistingOntologyMergeAddResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

public class ExistingOntologyMergeProjectWorkflow {
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
    public ExistingOntologyMergeProjectWorkflow(@Nonnull DispatchServiceManager dispatchServiceManager,
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


    public void start(ProjectId projectId, DocumentId documentId, List<OWLOntologyID> allOntologies, List<OWLOntologyID> selectedOntologies){
        selectExistingOntology(projectId, documentId, allOntologies, selectedOntologies);
    }

    private void selectExistingOntology(ProjectId projectId, DocumentId documentId, List<OWLOntologyID> allOntologies, List<OWLOntologyID> selectedOntologies){
        SelectExistingOntologyView view = new SelectExistingOntologyViewImpl(allOntologies);
        SelectExistingOntologyDialogController controller = new SelectExistingOntologyDialogController(view);
        controller.setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<OWLOntologyID>() {
            @Override
            public void handleHide(OWLOntologyID data, WebProtegeDialogCloser closer) {
                mergeIntoExistingOntology(projectId, documentId, selectedOntologies, view.getOntology());
                closer.hide();
            }
        });
        WebProtegeDialog.showDialog(controller);
    }

    private void mergeIntoExistingOntology(ProjectId projectId, DocumentId documentId, List<OWLOntologyID> selectedOntologies, OWLOntologyID targetOntology){
        dispatchServiceManager.execute(new ExistingOntologyMergeAddAction(projectId, documentId, selectedOntologies, targetOntology), new DispatchServiceCallbackWithProgressDisplay<ExistingOntologyMergeAddResult>(errorDisplay, progressDisplay) {
            @Override
            public String getProgressDisplayTitle() {
                return messages.merge_progressUploadingAndMerging();
            }

            @Override
            public String getProgressDisplayMessage() {
                return messages.merge_progressMergingIntoExisting();
            }

            @Override
            public void handleSuccess(ExistingOntologyMergeAddResult result){
                messageBox.showMessage(messages.merge_uploadedMergedSuccessfully());
            }
        });
    }
}
