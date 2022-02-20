package fr.slypy.aotuhc.roles;

public enum RolesName {

	ERWIN(Camps.ELDIA, "Erwin"), //ok
	EREN(Camps.ELDIA, "Eren"), //ok
	MIKASA(Camps.ELDIA, "Mikasa"), //ok
	LIVAI(Camps.ELDIA, "Livai"), //ok
	HISTORIA(Camps.ELDIA, "Historia"), //ok
	ARMIN(Camps.ELDIA, "Armin"), //ok
	HANSI(Camps.ELDIA, "Hansi"), //ok
	SASHA(Camps.ELDIA, "Sasha"), //ok
	CONNY(Camps.ELDIA, "Conny"), //ok
	JEAN(Camps.ELDIA, "Jean"), //ok
	SOLDIER(Camps.ELDIA, "Soldier"), //ok
	
	ZEKE(Camps.MAHR, "Zeke"), //ok
	PIECK(Camps.MAHR, "Pieck"), //ok
	LARA(Camps.MAHR, "Lara"), //ok
	REINER(Camps.MAHR, "Reiner"), //ok
	ANNIE(Camps.MAHR, "Annie"), //ok
	BERTHOLDT(Camps.MAHR, "Bertholdt"), //ok
	PORCO(Camps.MAHR, "Porco"), //ok
	MAGATH(Camps.MAHR, "Magath"), //ok
	GABY(Camps.MAHR, "Gaby"), //ok
	FALCO(Camps.MAHR, "Falco"), //ok changer si possible
	
	SMILINGTITAN(Camps.TITANS, "Smiling Titan"),
	DEVIANTTITAN(Camps.TITANS, "Deviant Titan"),
	SMALLTITAN(Camps.TITANS, "Small Titan"),
	MEDIUMTITAN(Camps.TITANS, "Medium Titan"),
	GREATTITAN(Camps.TITANS, "Great Titan");
	
    private Camps camp;
    private String name;
    
    private RolesName(Camps camp, String name) {
    	
        this.camp = camp;  
        this.name = name;
        
    }  
     
    public Camps getCamp() {  
    	 
        return this.camp; 
        
    }  
    
    @Override
    public String toString() {
    	
    	return name;
    	
    }
	
}