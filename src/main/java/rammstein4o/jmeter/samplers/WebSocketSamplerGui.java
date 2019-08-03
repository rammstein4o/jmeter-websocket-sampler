package rammstein4o.jmeter.samplers;

import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Dimension;

import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.BoxLayout.Y_AXIS;


public class WebSocketSamplerGui extends AbstractSamplerGui {
    private JTextField urlField;
    private JTextField tokenField;
    private JTextField publicTopicField;
    private JTextField privateTopicField;
    private JCheckBox appendSession;

    public WebSocketSamplerGui() {
        init();
    }

    private void init() {

        urlField = makeTextField();
        tokenField = makeTextField();
        publicTopicField = makeTextField();
        privateTopicField = makeTextField();
        appendSession = makeCheckboxField("Append Session To Topic", true);

        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);
        add(makeSettingsPanel(), BorderLayout.CENTER);
    }

    @Override
    public void clearGui() {
        super.clearGui();
        urlField.setText("");
        tokenField.setText("");
        publicTopicField.setText("");
        privateTopicField.setText("");
        appendSession.setSelected(true);
    }

    @Override
    public String getStaticLabel() {
        return "WebSocket Connection";
    }

    @Override
    public String getLabelResource() {
        return null;
    }

    @Override
    public void modifyTestElement(TestElement el) {
        configureTestElement(el);
        if (el instanceof WebSocketSampler) {
            WebSocketSampler sampler = (WebSocketSampler) el;
            sampler.setUrl(urlField.getText());
            sampler.setToken(tokenField.getText());
            sampler.setPublicTopic(publicTopicField.getText());
            sampler.setPrivateTopic(privateTopicField.getText());
            sampler.setAppendSession(appendSession.isSelected());
        }
    }

    @Override
    public void configure(TestElement el) {
        super.configure(el);
        if (el instanceof WebSocketSampler) {
            WebSocketSampler sampler = (WebSocketSampler) el;
            urlField.setText(sampler.getUrl());
            tokenField.setText(sampler.getToken());
            publicTopicField.setText(sampler.getPublicTopic());
            privateTopicField.setText(sampler.getPrivateTopic());
            appendSession.setSelected(sampler.getAppendSession());
        }
    }

    @Override
    public TestElement createTestElement() {
        WebSocketSampler element = new WebSocketSampler();
        modifyTestElement(element);
        return element;
    }

    private JTextField makeTextField() {
        JTextField field = new JTextField();
        field.setColumns(20);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, field.getMinimumSize().height));
        return field;
    }

    private JCheckBox makeCheckboxField(String label, boolean selected) {
        return new JCheckBox(label, selected);
    }

    private JPanel makeFieldPanel(JComponent field) {
        return makeFieldPanel("", field);
    }

    private JPanel makeFieldPanel(String label, JComponent field) {
        JPanel fieldPanel = new JPanel();
        {
            fieldPanel.setLayout(new BoxLayout(fieldPanel, X_AXIS));
            fieldPanel.setBorder(new EmptyBorder(0,0,10,0));

            if (!(field instanceof JCheckBox)) {
                JLabel urlLabel = new JLabel(label);
                urlLabel.setBorder(new EmptyBorder(0, 10, 0, 10));
                fieldPanel.add(urlLabel);
            }
            fieldPanel.add(field);
        }

        return fieldPanel;
    }

    private JPanel makeSettingsPanel() {
        JPanel boxPanel = new JPanel();
        {
            boxPanel.setLayout(new BoxLayout(boxPanel, Y_AXIS));
            boxPanel.setBorder(BorderFactory.createTitledBorder("WebSocket Settings"));
            boxPanel.add(makeFieldPanel("URL", urlField));
            boxPanel.add(makeFieldPanel("Auth Token", tokenField));
            boxPanel.add(makeFieldPanel("Public Topic", publicTopicField));
            boxPanel.add(makeFieldPanel("Private Topic", privateTopicField));
            boxPanel.add(makeFieldPanel(appendSession));
        }

        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BorderLayout());
        settingsPanel.add(boxPanel, BorderLayout.NORTH);
        return settingsPanel;
    }
}
