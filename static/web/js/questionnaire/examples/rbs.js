(function(q) {
	q.model = {
		title: 'Erhalt der Realschule RBS in Grevenbroich',
		isPublic: true,
		questions: [
			{
				number: 1,
				text: 'Sind Sie der Meinung, dass die Eltern mit dem Wegfall der Realschule RBS eine zu kleine Wahlmöglichkeit zwischen allen Schulformen haben?',
				supports: 'YES',
				facts: [{
					number: 1,
					text: 'Die Eltern hätten nach Wegfall der Realschule RBS noch die Wahl zwischen 1 Realschule, 3 Gymnasien, 2 Gesamtschulen und 1 Werkrealschule in Grevenbroich'
				}]
			},
			{
				number: 2,
				text: 'Sind Sie der Meinung, dass die Eltern mit dem Wegfall der Realschule RBS eine zu kleine Wahlmöglichkeit zwischen der Nachmittagsbetreuung haben?',
				supports: 'YES',
				facts: [
					{
						number: 1,
						text: 'Nur die Realschule RBS sowie die zwei Gesamtschulen bieten eine Nachmittagsbetreuung an'
					},
					{
						number: 2,
						text: 'Die Realschule RBS bietet eine wahlweise Nachmittagsbetreuung für alle Wochentage an'
					},
					{
						number: 3,
						text: 'Bei beiden Gesamtschulen in RBS ist der Nachmittagsunterricht verpflichtend. Der Nachmittagsunterricht findet an beiden Gesamtschulen nur an 3 Wochentagen statt'
					}]
			},
			{
				number: 3,
				text: 'Glauben Sie, dass die Anmeldezahlen für die Realschulen in Grevenbroich zukünftig abnehmen?',
				supports: 'NO',
				facts: []
			},
			{
				number: 4,
				text: 'Halten Sie die geplante Form der schrittweisen Auflösung der Realschule RBS für vertretbar?',
				supports: 'NO',
				facts: [
					{
						number: 1,
						text: 'Die Schule soll schrittweise geschlossen werden, indem keine neuen Schüler hinzukommen. Entsprechend der abnehmenden Schülerzahl würde auch die Lehrerzahl über die Jahre schrittweise verringert werden.'
					},
					{
						number: 2,
						text: 'Die Eltern befürchten, dass spätestens nach 2-3 Jahren kaum noch qualifizierte Lehrer für alle Unterrichtsfächer vorhanden sein werden und mit der Zeit ein solcher Notstand eintritt, dass viele Unterrichtsstunden ausfallen und beispielsweise ein Sportlehrer Englischunterricht geben muss.'
					}]
			}]
	};
})(questionnaire);