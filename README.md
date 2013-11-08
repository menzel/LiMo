_Einleitung_
Die Wirkungen von Umweltveränderungen auf lebende Organismen werden mit  biologischen Messverfahren ermittelt und dokumentiert. Der Verein Deutscher Ingenieure (VDI) stellt eine Reihe solcher Verfahren bereit. Eines dieser Biomonitoring-Verfahren hat die langfristige Beobachtung epiphytischer Flechten- und Moosbestände zum Inhalt (VDI 2003). Dabei wird der Epiphytenaufwuchs in Dauerquadraten an ausgewählten Trägerbäumen erfasst, indem eine transparente Folie an den Stamm angelegt und die äußeren Konturen der Flechten- und Moosthalli mit einem wasserfesten Stift auf der Folie nachgezeichnet werden. Eine wiederholte Erstellung von Folien am selben Stammausschnitt und die anschließende Auswertung ermöglichen es, die Epiphytenbestände über längere Zeiträume zu analysieren und Rückschlüsse auf sich verändernde Umweltbedingungen zu ziehen.
Die erste Fassung der VDI-Richtlinie mit dem Titel “Flächenbestimmung epiphytischer Flechten zur immissionsökologischen Langzeitbeobachtung” (VDI 2003) bediente sich zur Auswertung der Software “Flechtenanalyse”, die in der Arbeit von Kirschbaum et al. (2002) vorgestellt wurde. Im Zuge der Überarbeitung der Richtlinie sollte auch die Auswertungs-Software erneuert werden. Das Ergebnis wird in der vorliegenden Arbeit vorgestellt.  
Entwicklung
Die neue Software namens ‘LiMo-Analyse’ orientiert sich bezüglich der Aufgabe an der alten Version. Es handelt sich jedoch um ein von Grund auf neu gebautes Programm in der Programmiersprache Java. Dadurch kann das neue Programm unter jedem modernen Betriebssystem genutzt werden. Zudem ermöglicht diese moderne Sprache eine gute Erweiterbarkeit. Veröffentlicht wurde ‘LiMo-Analyse’ unter der GNU General Public License, Version 3. Dies bedeutet, dass jeder das Recht hat, die Software zu verändern und weiter zu entwickeln. LiMo-Analyse basiert auf dem ebenfalls quelloffenen Programm ImageJ, welches vom amerikanischen National Institute of Health zur medizinischen und wissenschaftlichen Bildanalyse entwickelt wurde.
Die Thallusanalyse ist eine sehr spezielle Arbeit mit Bildern, daher wurde eine komplett neue Oberfläche entwickelt um die Arbeit schneller und komfortabler zu machen. Neben den Standardwerkzeugen zur Bildbearbeitung wurden zudem spezielle Werkzeuge für die Analyse der Folien entwickelt. Zur Datenspeicherung wurde das Format .csv gewählt um die Verwertbarkeit für Tabellenkalkulationsprogramme zu gewährleisten und Datenbankanbindungen zu vereinfachen.
Das Programm kann von folgender Website kostenlos heruntergeladen werden:

_Ergebnisse_
Nach der Erstellung der Folie im Gelände wird diese farbig eingescannt (Abb. 1). Im rechten Teil dieser Folie befindet sich die Legende mit Daten über Standort und Datum, sowie eine Liste von häufig vorkommenden Arten. Im Aufnahmefeld (hier 20 x 20 cm) sind die Umrisse der Thalli zu sehen, jede Farbe gehört zu einer Art. Die Zuordnung ist durch die Farbmarkierung in der Legende gegeben.


Abb. 1: Beispiel einer gescannten Folie (Erfassung von Flechtenthalli an einem Baum der Dauerbeobachtungsfläche Gießen).
Die Bilddatei des Scans wird mit dem Programm LiMo-Analyse geöffnet. Das neue Programm ermöglicht die Folie durch Bildbearbeitungswerkzeuge vorzubereiten. So können etwa Lücken in den Thallusumrissen können geschlossen werden.
In der Ansicht des Bearbeitungsfensters des Programmes (Abb. 2) wurden in der  Beispielfolie die ersten vier Arten eingefärbt und zugewiesen. Auch hier ist jede Farbe zugehörig zu einer Art.
Eine Messung des Flächeninhaltes einer Art erfolgt, indem man nacheinander in alle Thalli der Art klickt und diese mit einer Farbe gefüllt werden. Das Statusfenster rechts oben zeigt eine Erfolgsmeldung und die Größe der gerade gefüllten Fläche. Danach wird die ID aus der Liste links ermittelt und der Messung zugeordnet. In der kleinen Tabelle rechts werden alle getätigten Messungen mit Farben angezeigt. Das Zwischenergebnis kann in der Tabelle am unteren Rand auf Wunsch ausgegeben werden.


Abb. 2: Ansicht der Bearbeitungsfenster während der Flächenauswertung reale Arten verwenden

Ist die Folie fertig ausgewertet, also alle Thalli aller Arten markiert und zugewiesen, kann das Bild mit gefüllten Thalli gespeichert und eine Ergebnistabelle exportiert werden. Ein Ausschnitt einer Ergebnistabelle ist in Tab. 1 zu sehen. Die Tabelle kann mit allen üblichen Tabellenkalkulations- oder Datenbankprogrammen geöffnet werden.
Tab. 1: Beispiel für ein Ergebnis der Folienauswertung


_Verbesserungen_
Das Programm bietet im Vergleich zur Vorversion folgende Neuerungen: 
Die Software kann unter den Betriebssystemen Windows, Mac und Linux genutzt werden.
Die Größe der zu untersuchenden Fläche ist frei wählbar. Neben dem DIN A 4-Format können beliebige weitere Formate verwendet werden.
Es gibt keine Begrenzung der maximalen Auflösung
Farbig eingescannte Folien können im Programm während der Auswertung farbig dargestellt werden. Die Zuordnung der Thalli zur Artenliste erfolgt am Bildschirm.  
Alle Füllschritte können rückgängig gemacht werden.
Die Stärke des im Gelände gezeichneten Thallusrahmens ist bei der anschließenden Auswertung einstellbar.
Die implementierten Funktionen zur Bildbearbeitung ermöglichen ein Nachbearbeiten der Scans, um beispielsweise Lücken in den gezeichneten Konturen zu schließen, ohne weitere Software zu benötigen. 
Es wurde eine Liste häufiger epiphytischer Moose hinzugefügt.  
Die Nomenklatur der Flechtentaxa wurde aktualisiert.
Mit der Erweiterung der Artenliste ist eine Suchfunktion zur schnellen Auswahl der Species sinnvoll geworden. 

Mit der vorliegenden Software steht ein Werkzeug zum Monitoring von epiphytischen Flechten- und Moosbeständen nach Vorgabe der Richtlinie des VDI 3957, Blatt 8 zur Verfügung. Das Programm kann darüber hinaus auch für Untersuchungen auf anderen Substraten (z. B. Gestein) oder weiterer Gruppen wie Algen oder lichenicoler Pilze verwendet werden.   
