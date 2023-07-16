package it.polito.tdp.gosales;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.gosales.model.Adiacenza;
import it.polito.tdp.gosales.model.Model;
import it.polito.tdp.gosales.model.Retailers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnAnalizzaComponente;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnSimula;

    @FXML
    private ComboBox<Integer> cmbAnno;

    @FXML
    private ComboBox<String> cmbNazione;

    @FXML
    private ComboBox<?> cmbProdotto;

    @FXML
    private ComboBox<Retailers> cmbRivenditore;

    @FXML
    private TextArea txtArchi;

    @FXML
    private TextField txtN;

    @FXML
    private TextField txtNProdotti;

    @FXML
    private TextField txtQ;

    @FXML
    private TextArea txtResult;

    @FXML
    private TextArea txtVertici;

    @FXML
    void doAnalizzaComponente(ActionEvent event) {
    	txtResult.clear();
    	Retailers retailer = cmbRivenditore.getValue();
    	if(retailer==null) {
    		txtResult.setText("Selezionare un rivenditore!");
    		return;
    	}
    	int dimesioneCompConnessa=model.getDimensioneComponenteConnessa(retailer);
    	int dimensioneSommaArchi = model.getSommaArchiComponenteConnessa(retailer);
    	txtResult.appendText("\n"+"La dimensione della componente connessa é : "+dimesioneCompConnessa+"\n");
    	txtResult.appendText("\n"+"La dimensione della somma degli archi della componente connessa é : "+dimensioneSommaArchi+"\n");
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	
    	String nazioneControllo = cmbNazione.getValue();
    	if (nazioneControllo == null) {
    		this.txtResult.setText("Scegli un Paese!");
    		return;
    	}
    	
    	Integer annoControllo = cmbAnno.getValue();
    	if (annoControllo == null) {
    		this.txtResult.setText("Scegli un anno!");
    		return;
    	}
    	
    	
    	int M = 0;
    	try {
    		M = Integer.parseInt(txtNProdotti.getText() );
    	} catch(NumberFormatException e) {
    		this.txtResult.setText("Carattere invalido! Il valore da inserire deve essere un numero intero!");
    		return;
    	}
    	 if(M<0) {
    		 this.txtResult.setText("Carattere invalido! Il valore da inserire deve essere un numero intero!");
    	 }
    	
    	int anno = cmbAnno.getValue();
    	String nazione = cmbNazione.getValue().toString();
    	model.creaGrafo(nazione, anno, M);
    	
    	if(model.grafoCreato()==true) {
    		
    		txtResult.appendText("Grafo creato!"+"\n");
    		txtResult.appendText("Vertici : " + model.getNumeroVertici()+"\n");
    		txtResult.appendText("Archi : "+model.getNumeroArchi()+"\n");
    		List<Retailers> Vertici = model.getListaVertici(nazione);
    		List<Adiacenza> Archi = model.getListaArchi(nazione, anno, M);
    		for(Retailers r : Vertici) {
    			txtVertici.appendText(r.toString());
    		}
    		for(Adiacenza aa : Archi) {
    			txtArchi.appendText(aa.toString());
    		}
    		
    		cmbRivenditore.setDisable(false);
    		btnAnalizzaComponente.setDisable(false);
    		btnSimula.setDisable(false);
    		cmbProdotto.setDisable(false);
    		cmbRivenditore.getItems().addAll(model.getListaVertici(nazione));
    		
    		
    	}
    	else {
    		txtResult.appendText("Grafo non creato!!!!"+"\n");
    	}
    	
    }

    @FXML
    void doSimulazione(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert btnAnalizzaComponente != null : "fx:id=\"btnAnalizzaComponente\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbAnno != null : "fx:id=\"cmbAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbNazione != null : "fx:id=\"cmbNazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbProdotto != null : "fx:id=\"cmbProdotto\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbRivenditore != null : "fx:id=\"cmbRivenditore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtArchi != null : "fx:id=\"txtArchi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtNProdotti != null : "fx:id=\"txtNProdotti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtQ != null : "fx:id=\"txtQ\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtVertici != null : "fx:id=\"txtVertici\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	cmbNazione.getItems().addAll(model.getCountries());
    	cmbAnno.getItems().addAll(2015,2016,2017,2018);
    	
    }

}
