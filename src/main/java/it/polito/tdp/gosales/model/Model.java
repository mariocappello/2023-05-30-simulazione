package it.polito.tdp.gosales.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.gosales.dao.GOsalesDAO;
import org.jgrapht.alg.connectivity.ConnectivityInspector;

public class Model {
	
	GOsalesDAO dao;
	Map<Integer,Retailers> idMap;
	Graph<Retailers,DefaultWeightedEdge> grafo;
	
	public Model() {
		dao= new GOsalesDAO();
		
		idMap=new HashMap<>();
	    dao.getAllRetailerPerMappa(idMap);
	}
	
	public void creaGrafo( String nazione,int anno, int M) {
		grafo = new SimpleWeightedGraph<Retailers,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		List<Retailers> listaVertici = dao.creaVertici(nazione);
		Graphs.addAllVertices(grafo, listaVertici);
		
		List<Adiacenza> listaArchi = dao.creaArchi(nazione,anno,M,idMap);
		for(Adiacenza a : listaArchi) {
			Graphs.addEdgeWithVertices(grafo, a.getRc1(), a.getRc2(), a.getPeso());
			
		}
		
	}
	
	
	
	public List<Retailers> getListaVertici(String nazione) {
		List<Retailers> Vertici = dao.creaVertici(nazione);
		Collections.sort(Vertici);
		return Vertici;
	}
	
	public List<Adiacenza> getListaArchi(String nazione,int anno, int M) {
		List<Adiacenza> Archi = dao.creaArchi(nazione, anno, M, idMap);
		Collections.sort(Archi);
		return Archi;
	}
	
	
	
	public int getDimensioneComponenteConnessa(Retailers retail) {
		ConnectivityInspector<Retailers,DefaultWeightedEdge> inspector = new ConnectivityInspector<Retailers,DefaultWeightedEdge>(grafo);
		return inspector.connectedSetOf(retail).size();
	}
	
	public int getSommaArchiComponenteConnessa(Retailers retail) {
		
		ConnectivityInspector<Retailers,DefaultWeightedEdge> inspector = new ConnectivityInspector<Retailers,DefaultWeightedEdge>(grafo);
		
		// il connectedSetOf() ritorna una lista di vertici connessi del grafo al parametro passato (retail)
		
		Set<Retailers> VerticiConnessi = inspector.connectedSetOf(retail);
		int peso=0;
		
		//prendo gli archi uno ad uno e verifico se i suoi vertici sono nella componente connessa
		
		for(DefaultWeightedEdge e : grafo.edgeSet()) {
			if(VerticiConnessi.contains(grafo.getEdgeSource(e)) 
					&& VerticiConnessi.contains(grafo.getEdgeTarget(e))) {
				peso+=grafo.getEdgeWeight(e);
			}
		}
		return  peso;
	}
	
	
	
	
	
	
	public List<String> getCountries() {
		return dao.getAllCauntries();
	}
	
	public int getNumeroVertici() {
		if(grafo!=null) {
			return grafo.vertexSet().size();
		}
		else {
			return 0;	
		}
	}
	
	public int getNumeroArchi() {
		if(grafo!=null) {
			return grafo.edgeSet().size();
		}
		else {
			return 0;	
		}
	}

	public boolean grafoCreato() {
		if(grafo!=null) {
			return true;
		}
		else
		return false;
	}
	
}
