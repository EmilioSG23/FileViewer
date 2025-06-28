package com.emiliosg23.models;

import com.emiliosg23.models.enums.Modes;
import com.emiliosg23.models.infos.FileInfo;
import com.emiliosg23.models.infos.Info;
import com.emiliosg23.models.tdas.trees.MultiTree;

import javafx.scene.layout.Pane;

public class AppLogic {
	private final int numLevelLimit=20;
	private final int numLevelTitleLimit=3;
	
	private boolean fileExtensionMode;
	private boolean acumulativeMode;
	private boolean executableMode;

	private boolean showFilenames;

	private MultiTree<Info> directoryTree;
	
	private int limitLevel;
	private int limitLevelTitle;

	public boolean changeMode(Modes mode){
		if(mode == Modes.ACUMULATIVE)
			return this.acumulativeMode = !this.acumulativeMode;
		if(mode == Modes.EXECUTABLE)
			return this.executableMode = !this.executableMode;
		if(mode == Modes.FILE_EXTENSION)
			return this.fileExtensionMode = !this.fileExtensionMode;
		throw new RuntimeException("Selected mode is not available");
	}

	public boolean showFilenames(){
		return this.showFilenames = !this.showFilenames;
	}

	public int addLimitLevel(){
		this.limitLevel++;
		return this.limitLevel;
	}
	public int substractLimitLevel(){
		this.limitLevel--;
		return this.limitLevel;
	}
	public int addLimitTitleLevel(){
		this.limitLevelTitle++;
		return this.limitLevelTitle;
	}
	public int substractLimitTitleLevel(){
		this.limitLevelTitle--;
		return this.limitLevelTitle;
	}

	public void reset(){
			/*cambiarModoArchivoExtension(false);
			mostrarNombres(true);
			cambiarModoEjecutable(false);
			cambiarModoAcumulativo(false);
			iniciarDirectorio("C:\\");
			setLimitLevel(8);
			setLimitLevelTitle(2);*/
    }

		public void update(){

		}

    private void iniciarDirectorio(String directorio,boolean save){
        /*
         boolean vertical=false;
        try{
            this.directoryTree=TreeFileUtils.createTreeDirectory(directorio,vertical,this.limitLevel);
            if(fileExtensionMode){
                this.directoryTree=TreeFileUtils.createTreeDirectoryExtension(this.directoryTree,vertical,this.limitLevel);
                if(acumulativeMode)
                    this.directoryTree=TreeFileUtils.createTreeDirectoryAcumulative(this.directoryTree,vertical);
            }
        }catch(Exception e){
            alert.close();
            btnActualizar.setDisable(true);
            AppUtils.showErrorAlert("Ha ocurrido un error al momento de cargar los directorios y/o archivos");
            return;
        }
        initializeTreeMaps(directoryTree, paneTreeMap, directoryTree.getRoot().getContent().getSize(), limitLevelTitle, limitLevel,vertical);
        fillPaneTreeMap(paneTreeMap,directoryTree,true,this.showFilenames);
        alert.close();*/
    }
    private void iniciarDirectorio(String directorio){
        //iniciarDirectorio(directorio,true);
    }
    private void iniciarDirectorio(){
        //iniciarDirectorio(txtDirectorio.getText(),false);
    }
    
    private void initializeTreeMaps(MultiTree<FileInfo> directoryTree,Pane paneRoot,long sizeParent,int limitLevelTitle,int limitLevel,boolean vertical){
        /*MultiTreeNode<FileInfo> root=directoryTree.getRoot();
        if(limitLevelTitle>0)
            root.getContent().initializePresentationNode(sizeParent, paneRoot, true,vertical);
        else if(limitLevel>0)
            root.getContent().initializePresentationNode(sizeParent, paneRoot, false,vertical);
        for(MultiTree<FileInfo> child:directoryTree.getRoot().getChildren())
                initializeTreeMaps(child, root.getContent().getPresentationNode().getTreeMap(), root.getContent().getSize(), limitLevelTitle-1, limitLevel-1,!vertical);
    */}
    private void fillPaneTreeMap(Pane pane,MultiTree<FileInfo> directoryTree, boolean resetPane,boolean showFilenames){
        /*if(resetPane)
            resetPane(pane);
        MultiTreeNode<FileInfo> root=directoryTree.getRoot();
        if(!root.getChildren().isEmpty())
            for(MultiTree<FileInfo> child:root.getChildren())
                fillPaneTreeMap(root.getContent().getPresentationNode().getTreeMap(),child,false,showFilenames);
        pane.getChildren().add(directoryTree.getRoot().getContent().createPresentationNode(showFilenames));*/
    }
}
