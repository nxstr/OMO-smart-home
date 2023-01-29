# OMO-smart-home


## Projekt: Smart Home

Projekt představuje aplikaci pro virtuální simulaci smart home. 

Projekt má dva scenario pro různé konfigurace domy.

# Funkční požadavky

F1.	Entity se kterými pracujeme je dům, patro v domu, senzor, zařízení (=spotřebič), osoba, venkovní prostředí pro sport lyži, kolo, domácí zvíře jiného než hospodářského typu

F2.	Zařízení mají stav, který se mění když se děje nějaká interakce přes entitu nebo event.

F3.	Vybrane spotřebiče maji kapacitu, kterou občas je třeba doplnít (fridge, coffee machine, pet feeder). Zařizení dishwasher a washing machine po ukončení práce musí byt vyprazdněno a doplněno.

F4.	Jednotlivá zařízení mají API na sběr dat o tomto zařízení. O zařízeních sbíráme data jako spotřeba elektřiny

F5.	Jednotlivé osoby a zvířata mohou provádět aktivity(akce), které mají nějaký efekt na zařízení nebo jinou osobu.

F6.	Jednotlivá zařízení a osoby se v každém okamžiku vyskytují v jedné místnosti (pokud nesportují) a náhodně generují eventy (eventem může být důležitá informace a nebo alert)

F7.	Eventy jsou přebírány a odbavovány vhodnou osobou (osobami) nebo zařízením (zařízeními). Např.:
- čidlo na temperaturu  => aktivace klimatizace
- jistič (výpadek elektřiny) => vypnutí všech nedůležitých spotřebičů (v provozu zůstávají pouze ty nutné)
- čidlo na vypadek vody => máma -> vypnutí všech spotřebičů, ktere použivaji vodu
- Zařízení přestalo fungovat => generovani zadaní pro dospele
- V lednici došlo jídlo => generovani zadaní pro dospele

F8.	Vygenerování reportů (jsou ve složce resources):
- HouseConfigurationReport: veškerá konfigurační data domu zachovávající hieararchii - dům -> patro -> místnost -> spotřebič atd. Plus jací jsou obyvatelé domu.
- EventReport: report eventů a jejich cíle (jaká entita event odbavila)
- ActivityReport: Report akcí (aktivit) jednotlivých osob a zvířat.
- ConsumptionReport: Kolik jednotlivé spotřebiče spotřebovaly elektřiny, kolikrát které zařízení bylo použite, rozbite.

F9.	Rozbite zařizení se zapiše do tasklistu pro dospěle. TaskList je jeden pro všechne dospěle. Při rozbití zařízení musí obyvatel domu prozkoumat dokumentaci k zařízení.

F10. Rodina je aktivní a volný čas tráví zhruba v poměru (50% používání spotřebičů v domě a 50% sport kdy používá sportovní náčiní kolo nebo lyže). Když není volné zařízení nebo sportovní náčiní, tak osoba čeká.

F11. Smart Home má 4 strategii pro ruzné hodiny: Morning, Afternoon a Evening zapnou vybrané spotřebiče a vypnou za určitou dobu. 
Night strategie změní stav všech aktivných spotřebičů na IDLE a vygeneruje report za den.

F12. V simulace jsou EventGeneratory, ktere v nahodnou hodinu generuji eventy, které chyti sensory.
Temperature sensor zapne klimatizace v te mistnosti kde se vyskytuje event, pokud tam klimatizace je.
Fire sensor zapne Fire suppression v teto mistnosti, která vypné všechne spotřebiče. Všechne entity jdou ven dokud fire suppression se nevypne .
Water a Electricity sensors reaguji na vypadek a zapnuti vody nebo elektřiny.
Entity sensor reaguje, kdyz nějaká osoba nebo zvíře jde ven nebo se vrati. Na zakladě toho otevře/zavře zámek.

F13. V připadě, že v konfiguraci důmu nebudou ukazane dostačující číslo zažizení: fire suppression(zaleži na počtu mistnosti), pet feeder (zalezi na počtu zviřat), zařizení budou automaticky doplněna.
Taky, sensory se doplnují automaticky, v každe mistnosti fire a temperature sensory, po jednemu sensoru typu entity, water, electricity na dům.

# Desing patterny
- Factory :DeviceFactory, LivingEntityFactory, SportEquipmentFactory, RoomFactory
- Builder: HouseBuilder (inner class in House)
- Singleton: Observer, TaskList, House, OutsideArea, all Factory classes
- StateMachine: state package
- Observer: Observer
- Strategy: strategy package
- Bridge: events package
- Lazy Initialization: Manual, TaskList, Observer, OutsideArea, eventGenerator classes
