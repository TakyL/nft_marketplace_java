
# Sommaire 


## [Principe](#principe)

## [Dépendances d'installation](#dépendances-dinstallation)

## [Guide d'installation](#guide-dinstallation)

## [Problèmes connus](#problèmes-connus)



# Principe

Le principe de ce projet est de pouvoir certifier de manière simple les chansons d'un auteur au sein de la blockchain. Pour ce faire, cette application JAVA permet à une addresse d'enrengistrer des titres de chansons sous forme de NFT en échange d'une taxe à hauteur de 0.05 gwei.
Il peut également consulter les chansons  et les retirer.

Hebergé chez polygon amoy, et en utilisant les contrats suivants : [market]() et [nft]() dont la code source se trouve [ici](https://github.com/TakyL/nft_marketplace_solidity).
La monnaie utilisé est le POL. Si vous n'avez pas de wallet POL, vous pouvez utiliser celui là : [wallet address](0x501AFfB9402246e0E522aDaf7e2638A41859a426) & [key](0x116bbe4fb13bf060ad8497412aa1be43cb2be587446a37832da2d13a50a2681d)
Cette application est l'implementation simple de ce projet en Java en utilisant le framework Javafx pour l'IHM.

## Dépendances d'installation

-  JDK Java avec Javafx inclus : version LTS(17 mini), Distributions : [Zulu](https://www.azul.com/downloads/?package=jdk#zulu), [Liberica](https://bell-sw.com/pages/downloads/#jdk-17-lts) 
- OS conseillé : W11 (tests réalisés sur cet OS), Linux (pas testé)

## Guide d'installation

- Clone ce repo
- Build le projet
- Lancer l'app : Main
- Saisir une addresse de wallet et sa private key associée sur le menu lancé (login)

## Problèmes connus

