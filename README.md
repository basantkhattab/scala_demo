# Ryhmän nimi
Matikan rakastajat
# Jäsenet
- Denis Kuznetsov
- Basant Khattab
# Kuvaus
Projektissa on 2 demo effektiä: Aallot ja Pallo
## Aallot
Effektissä on simppelit aallot. Päätavoite on just hypnoosi, joten se kestää loputtomasti
Aallot ovat tehtyjä jakojäännöksen avulla keskuksesta etäisyyden ja ajan perustella. Ajan avulla allot liikkuvat keskuksesta.
Ensin ohjelma päättää millaista ympyrä piti piirtää ja sen jälkeen päättää mitä väriä käyttää. Periaatteessa meillä on aina x ja y suora (ne ovat kohtisuorat ja heidän risteyksen piste on aina aaltojen keskuksessa) ja joka tickilla me pyöritään niitä kulmalla. Näin meillä on 4 osaa, ja jokainen on omassa värissä
## Pallo
Tämä effekti näyttää simppelilta, mutta toteutus on paljon ja paljon vaikeampi kuin oli aalto effektissä. 
Pikseli tässä on pieni ympyrä, koska se jostain syystä näyttää vaan paremmalta.
Joka tickilla ohjelma päättää jokaiselle pikselille onko se pallon pikseli vai ei (jos ei, sitten se on musta ja siinä kaikki). Jos etäisyys keskuksesta on vähemmän kuin RADIUS, sitten se on pallon pikseli. Siihen lasketaan z muuttujaa pallon kaavan avulla ( (x-xCentr)^2 + (y-yCentr)^2 + z^2 = RADIUS^2 ), niin me tiedetään pikselin sijainti avaruudessa.
Meillä on aina axis vectori, joka on pyöriminen akseli. Sen perustella otetaan oikeaa kantaa (right-handed basis) - vielä kaksi vektoria, jotka ovat kaikki kohtisuorat. Jos niiden pistetulot ovat molemmat + vai molemmat -, sitten se on ensimmäinen väri. Jos ei - toinen. Sen jälkeen jokaisen piskelin väri pimennetään ensin aaltojen funktion avulla ja sitten valon funktion avulla. Ne molemmat laskevat etäisyyttä jostakin pisteestä ja sen perusteella pimentävät piskelia.
Tick funktiossa kutsutaan rotate funktiota, joka pyörittää kannan vectoria akselin ympärillä. Se funktio käyttää kvaterniota siihen.
