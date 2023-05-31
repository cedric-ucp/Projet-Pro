

					####################################
					#				 				   #
					#			AUDIT TOOL 			   #
					#								   #
					####################################
					
					
	I. Description
	
		AUDIT TOOL est un outil qui permet d'effectuer des scans sur des cibles et d'effectuer des audits complets sur des cibles. 
		C'est un outil développé en Java, il est accessible pour le grand public et n'est pas réservé uniquement au monde de l'IT.
	
	II. Prérequis 
		
		Pour utiliser parfaitement AUDIT TOOL, il nécessaire de prendre en compte que pour désigner une cible deux options sont disponibles :
		
			- Par addresse ip :
			
				L'addresse ip fournie doit absolument être une adresse publique.
				Avec le format habituel d'une adresse ip (xxx.xxx.xxx.xxx) avec xxx >= 255.
				Tout autre format d'adresse ip sera refusé et sera considéré comme une erreur.
				
			- Par url web :
			
				L'url fournie doit comprendre le protocole, le domaine et le sous-domaine.
				Par exemple pour effectuer une requête sur la cible "Google"
				L'url à fournir serait 	"https://google.fr" ou "https://www.google.fr". 
				L'outil se chargera de décrypter l'url afin d'y soutirer le domaine pour effectuer la requête. 
				Tout autre format d'url sera refusé. 
		
		AUDIT TOOL a été créée en JAVA, il a donc besoin que sur la machine hôte se trouve un JAVA SE Development Kit disponible.
		Il est donc important d'avoir un JAVA SE Development Kit sur son système et plus précisément le JAVA SE Development Kit version 18.0.2.1.
		Pour l'installer sur son système, se référer à ce lien https://www.oracle.com/java/technologies/javase/jdk18-archive-downloads.html
	
	III. Informations Utiles
	
		AUDIT TOOL permet le choix de générer un rapport après avoir avoir effectué un audit sur une cible.
		Ce rapport est un document PDF généré par une librairie externe.

		Pour quitter/fermer l'outil appuyer simultanément sur les touches "ctrl" + "c".
		