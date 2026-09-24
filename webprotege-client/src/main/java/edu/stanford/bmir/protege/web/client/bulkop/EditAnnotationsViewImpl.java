package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.common.base.Preconditions;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.primitive.DefaultLanguageEditor;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorImpl;
import edu.stanford.bmir.protege.web.shared.bulkop.NewAnnotationData;
import edu.stanford.bmir.protege.web.shared.bulkop.Operation;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static javaemul.internal.InternalPreconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Sep 2018
 */
public class EditAnnotationsViewImpl extends Composite implements EditAnnotationsView {

    interface ReplaceAnnotationsValueViewImplUiBinder extends UiBinder<HTMLPanel, EditAnnotationsViewImpl> {

    }

    private static ReplaceAnnotationsValueViewImplUiBinder ourUiBinder = GWT.create(ReplaceAnnotationsValueViewImplUiBinder.class);

    @UiField(provided = true)
    protected AnnotationSimpleMatchingCriteriaViewImpl criteriaView;


    @UiField
    Label editOperationLabel;

    @UiField
    Label newPropertyLabel;

    @UiField
    Label specifyPropertyLabel;

    @UiField
    Label newValueLabel;

    @UiField
    Label specifyValueLabel;

    @UiField
    Label newLangTagLabel;

    @UiField
    Label specifyLangTagLabel;

    @UiField
    ListBox operationCombo;

    @UiField
    HTMLPanel newAnnotationsSection;

    @UiField(provided = true)
    DefaultLanguageEditor newLangTagField;

    @UiField
    TextBox newLexicalValueField;

    @UiField(provided = true)
    PrimitiveDataEditorImpl newPropertyField;

    @UiField
    HTMLPanel propertyGroup;

    @UiField
    HTMLPanel valueGroup;

    @UiField
    RadioButton matchedPropertyRadio;

    @UiField
    RadioButton otherPropertyRadio;

    @UiField
    RadioButton matchedValueRadio;

    @UiField
    RadioButton otherValueRadio;

    @UiField
    RadioButton matchedLangTagRadio;

    @UiField
    RadioButton otherLangTagRadio;

    @UiField
    HTMLPanel langTagGroup;

    private final Messages messages;

    @Inject
    public EditAnnotationsViewImpl(AnnotationSimpleMatchingCriteriaViewImpl view,
                                   PrimitiveDataEditorImpl newPropertyField,
                                   DefaultLanguageEditor newLangTagField,
                                   Messages messages) {
        this.criteriaView = Preconditions.checkNotNull(view);
        this.newPropertyField = newPropertyField;
        this.newLangTagField = newLangTagField;
        this.messages = Preconditions.checkNotNull(messages);
        initWidget(ourUiBinder.createAndBindUi(this));
        editOperationLabel.setText(messages.bulkop_editOperation());
        operationCombo.setItemText(0, messages.bulkop_replaceMatchedAnnotations());
        operationCombo.setItemText(1, messages.bulkop_deleteMatchedAnnotations());
        operationCombo.setItemText(2, messages.bulkop_augmentMatchedAnnotations());
        newPropertyLabel.setText(messages.bulkop_newProperty());
        matchedPropertyRadio.setText(messages.bulkop_matchedProperty());
        otherPropertyRadio.setText(messages.bulkop_otherProperty());
        specifyPropertyLabel.setText(messages.bulkop_specifyProperty());
        newValueLabel.setText(messages.bulkop_newValue());
        matchedValueRadio.setText(messages.bulkop_matchedValue());
        otherValueRadio.setText(messages.bulkop_otherValue());
        specifyValueLabel.setText(messages.bulkop_specifyValue());
        newLangTagLabel.setText(messages.bulkop_newLangTag());
        matchedLangTagRadio.setText(messages.bulkop_matchedLangTag());
        otherLangTagRadio.setText(messages.bulkop_otherLangTag());
        specifyLangTagLabel.setText(messages.bulkop_specifyLangTag());
        updateOperationSection();
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        criteriaView.requestFocus();
    }

    @UiHandler("matchedPropertyRadio")
    protected void handleMatchedPropertyRadioChanged(ValueChangeEvent<Boolean> event) {
        propertyGroup.setVisible(!event.getValue());
    }

    @UiHandler("matchedValueRadio")
    protected void handleMatchedValueRadioChanged(ValueChangeEvent<Boolean> event) {
        valueGroup.setVisible(!event.getValue());
    }

    @UiHandler("matchedLangTagRadio")
    protected void handleMatchedLangTagRadioChanged(ValueChangeEvent<Boolean> event) {
        langTagGroup.setVisible(!event.getValue());
    }

    @UiHandler("otherPropertyRadio")
    protected void handlePropertyRadioChanged(ValueChangeEvent<Boolean> event) {
        propertyGroup.setVisible(event.getValue());
    }

    @UiHandler("otherValueRadio")
    protected void handleValueRadioChanged(ValueChangeEvent<Boolean> event) {
        valueGroup.setVisible(event.getValue());
    }

    @UiHandler("otherLangTagRadio")
    protected void handleLangTagRadioChanged(ValueChangeEvent<Boolean> event) {
        langTagGroup.setVisible(event.getValue());
    }


    @Nonnull
    @Override
    public Operation getOperation() {
        return Operation.values()[operationCombo.getSelectedIndex()];
    }

    @Nonnull
    @Override
    public Optional<OWLAnnotationProperty> getAnnotationProperty() {
        if (criteriaView.isMatchProperty()) {
            return criteriaView.getProperty().map(OWLAnnotationPropertyData::getEntity);
        }
        else {
            return Optional.empty();
        }
    }

    @Nonnull
    @Override
    public Optional<String> getLexcialValueExpression() {
        if(criteriaView.isMatchLexicalValue()) {
            return Optional.of(criteriaView.getLexicalValueExpression());
        }
        else {
            return Optional.empty();
        }
    }

    private Optional<OWLAnnotationProperty> getNewAnnotationProperty() {
        if(otherPropertyRadio.getValue()) {
            return newPropertyField.getValueAsAnnotationPropertyData()
                    .map(OWLAnnotationPropertyData::getEntity);
        }
        else {
            return Optional.empty();
        }
    }

    private Optional<String> getNewLexicalValueExpression() {
        if(otherValueRadio.getValue()) {
            return Optional.of(newLexicalValueField.getValue());
        }
        else {
            return Optional.empty();
        }
    }

    private Optional<String> getNewLangTag() {
        if(otherLangTagRadio.getValue()) {
            return newLangTagField.getValue();
        }
        else {
            return Optional.empty();
        }
    }

    @Nonnull
    @Override
    public NewAnnotationData getNewAnnotationData() {
        return NewAnnotationData.get(
                getNewAnnotationProperty(),
                getNewLexicalValueExpression(),
                getNewLangTag()
        );
    }

    @Override
    public Optional<String> getLangTagExpression() {
        return criteriaView.getLangTag();
    }

    @Override
    public boolean isLexicalValueExpressionRegEx() {
        return criteriaView.isLexicalValueRegEx();
    }

    @UiHandler("operationCombo")
    public void operationComboChange(ChangeEvent event) {
        updateOperationSection();
    }

    private void updateOperationSection() {
        newAnnotationsSection.setVisible(getOperation() != Operation.DELETE);
    }
}