package bg.tu_varna.sit.f24621702.task.xml;

import bg.tu_varna.sit.f24621702.task.interfaces.StorageStrategy;
import bg.tu_varna.sit.f24621702.task.models.Automaton;
import bg.tu_varna.sit.f24621702.task.models.AutomatonBuilder;
import bg.tu_varna.sit.f24621702.task.models.Transition;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XmlManager implements StorageStrategy {

    @Override
    public List<Automaton> load(String filePath) throws Exception {
        List<Automaton> automata = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return automata;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        NodeList nodeList = doc.getElementsByTagName("automaton");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            String id = element.getAttribute("id");
            AutomatonBuilder autoBuilder = new AutomatonBuilder(id);

            NodeList initNode = element.getElementsByTagName("initialState");
            if (initNode.getLength() > 0) autoBuilder.setInitial(initNode.item(0).getTextContent().trim());

            NodeList finalWrapper = element.getElementsByTagName("finalStates");
            if (finalWrapper.getLength() > 0) {
                NodeList fStates = ((Element) finalWrapper.item(0)).getElementsByTagName("state");
                for (int k = 0; k < fStates.getLength(); k++) autoBuilder.addFinal(fStates.item(k).getTextContent().trim());
            }

            NodeList transList = element.getElementsByTagName("transition");
            for (int j = 0; j < transList.getLength(); j++) {
                Element tElem = (Element) transList.item(j);
                autoBuilder.addTransition(
                        tElem.getElementsByTagName("from").item(0).getTextContent().trim(),
                        tElem.getElementsByTagName("to").item(0).getTextContent().trim(),
                        tElem.getElementsByTagName("symbol").item(0).getTextContent().trim()
                );
            }
            automata.add(autoBuilder.build());
        }
        return automata;
    }

    @Override
    public void save(String filePath, List<Automaton> automata) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element root = doc.createElement("automata");
        doc.appendChild(root);

        for (Automaton a : automata) {
            Element autoElem = doc.createElement("automaton");
            autoElem.setAttribute("id", a.getId());

            Element init = doc.createElement("initialState");
            init.setTextContent(a.getInitialState());
            autoElem.appendChild(init);

            Element finals = doc.createElement("finalStates");
            for(String f : a.getFinalStates()) {
                Element s = doc.createElement("state");
                s.setTextContent(f);
                finals.appendChild(s);
            }
            autoElem.appendChild(finals);

            Element transitionsElem = doc.createElement("transitions");
            for (Transition t : a.getTransitions()) {
                Element trans = doc.createElement("transition");
                Element from = doc.createElement("from"); from.setTextContent(t.getFromState());
                Element to = doc.createElement("to"); to.setTextContent(t.getToState());
                Element sym = doc.createElement("symbol"); sym.setTextContent(t.getSymbol());
                trans.appendChild(from); trans.appendChild(to); trans.appendChild(sym);
                transitionsElem.appendChild(trans);
            }
            autoElem.appendChild(transitionsElem);
            root.appendChild(autoElem);
        }

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.transform(new DOMSource(doc), new StreamResult(new File(filePath)));
    }
}