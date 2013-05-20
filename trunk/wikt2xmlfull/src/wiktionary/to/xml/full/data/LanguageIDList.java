package wiktionary.to.xml.full.data;

import java.util.HashMap;
import static wiktionary.to.xml.full.data.LanguageID.*;

/**
 * List of languages used in Wiktionary, especially
 * in translation sections inside definitions.
 * See http://meta.wikimedia.org/wiki/Wiktionary#List_of_Wiktionaries
 * In addition to languages that have a Wiktionary, certain old languages used
 * in translations have two letter codes. E.g. OE = Old English
 * 
 * @author Joel Korhonen
 * 2012-07-25 First version
 * 2012-07-27 Added a lot of languages
 * 2012-07-30 Added more languages
 * 2012-07-31 Added more languages
 */
public class LanguageIDList {
	// Key is two letter code of language, e.g. en
	public static HashMap<String, LanguageID> langIDs = null;
	// Key is description of language, e.g. English
	public static HashMap<String, LanguageID> langIDs_Desc = null;
/*
№ 	Language 	Language (local) 	Wiki 	Good 	Total 	Edits 	Admins 	Users 	Active Users 	Images 	Updated
150 	Afar 	Afar 	aa 	1 	69 	6734 	0 	450 	0 	0 	2012-07-13 00:31:09
153 	Abkhazian 	Аҧсуа 	ab 	0 	226 	8939 	0 	468 	0 	0 	2012-07-13 00:31:10
52 	Afrikaans 	Afrikaans 	af 	15765 	21952 	106966 	4 	2064 	15 	969 	2012-07-13 00:30:24
165 	Akan 	Akana 	ak 	0 	55 	4344 	0 	576 	0 	0 	2012-07-13 00:31:30
152 	Alemannic 	Alemannisch 	als 	0 	1221 	15234 	1 	626 	0 	0 	2012-07-13 00:31:03
120 	Amharic 	አማርኛ 	am 	201 	2016 	8131 	1 	1012 	8 	0 	2012-07-13 00:31:10
103 	Aragonese 	Aragonés 	an 	691 	4203 	13399 	4 	1122 	6 	2 	2012-07-13 00:30:42
86 	Anglo-Saxon 	Englisc 	ang 	2164 	5841 	33133 	0 	1899 	10 	6 	2012-07-13 00:30:17
32 	Arabic 	العربية 	ar 	47633 	61790 	512525 	7 	7643 	31 	8 	2012-07-13 00:30:25
148 	Assamese 	অসমীয়া 	as 	16 	238 	4950 	0 	448 	0 	0 	2012-07-13 00:31:12
57 	Asturian 	Asturianu 	ast 	11416 	14981 	84772 	1 	1329 	13 	1 	2012-07-13 00:30:37
167 	Avar 	Авар 	av 	0 	52 	4835 	0 	412 	0 	0 	2012-07-13 00:31:30
146 	Aymara 	Aymar 	ay 	27 	678 	8917 	0 	822 	5 	0 	2012-07-13 00:30:39
70 	Azerbaijani 	Azərbaycanca 	az 	6157 	9198 	60284 	1 	1695 	16 	17 	2012-07-13 00:31:12
83 	Belarusian 	Беларуская 	be 	2483 	5141 	25101 	1 	1146 	11 	0 	2012-07-13 00:30:49
40 	Bulgarian 	Български 	bg 	26786 	897175 	1031082 	3 	6967 	18 	22 	2012-07-13 00:30:04
155 	Bihari 	भोजपुरी 	bh 	0 	206 	4822 	0 	406 	0 	0 	2012-07-13 00:31:31
159 	Bislama 	Bislama 	bi 	0 	194 	4765 	0 	371 	0 	0 	2012-07-13 00:31:31
168 	Bambara 	Bamanankan 	bm 	0 	48 	4062 	0 	349 	0 	0 	2012-07-13 00:31:31
119 	Bengali 	বাংলা 	bn 	237 	2321 	14767 	1 	1245 	9 	2 	2012-07-13 00:30:52
161 	Tibetan 	Bod skad 	bo 	0 	152 	4745 	0 	357 	0 	0 	2012-07-13 00:31:13
44 	Breton 	Brezhoneg 	br 	22440 	29226 	105154 	1 	1559 	16 	7 	2012-07-13 00:31:05
79 	Bosnian 	Bosanski 	bs 	3357 	6657 	21444 	5 	1187 	10 	0 	2012-07-13 00:30:32
37 	Catalan 	Català 	ca 	35047 	42320 	251958 	4 	3524 	26 	4 	2012-07-13 00:30:29
169 	Chamorro 	Chamoru 	ch 	0 	48 	3454 	0 	395 	0 	0 	2012-07-13 00:31:32
121 	Cherokee 	ᏣᎳᎩ 	chr 	200 	1025 	8331 	0 	944 	9 	5 	2012-07-13 00:31:33
88 	Corsican 	Corsu 	co 	2103 	4499 	29062 	1 	1167 	8 	17 	2012-07-13 00:30:19
170 	Cree 	Nehiyaw 	cr 	0 	43 	3286 	0 	369 	0 	0 	2012-07-13 00:31:33
35 	Czech 	Čeština 	cs 	36647 	53809 	317731 	5 	7313 	36 	1 	2012-07-13 00:30:23
91 	Kashubian 	Kaszëbsczi 	csb 	1496 	3258 	20652 	1 	1076 	8 	0 	2012-07-13 00:31:13
54 	Welsh 	Cymraeg 	cy 	13680 	16922 	60373 	3 	1317 	13 	6 	2012-07-13 00:30:33
64 	Danish 	Dansk 	da 	7282 	14199 	80113 	6 	3228 	18 	1 	2012-07-13 00:30:17
14 	German 	Deutsch 	de 	219012 	245749 	2521099 	24 	59535 	214 	75 	2012-07-13 00:30:05
142 	Divehi 	ދިވެހިބަސް 	dv 	79 	701 	5561 	3 	855 	8 	2 	2012-07-13 00:31:29
154 	Dzongkha 	ཇོང་ཁ 	dz 	0 	208 	4831 	0 	364 	0 	0 	2012-07-13 00:31:14
7 	Greek 	Ελληνικά 	el 	370371 	387924 	2857857 	7 	9472 	41 	64 	2012-07-13 00:30:10
1 	English 	English 	en 	3049003 	3289599 	17523433 	98 	749661 	1035 	20 	2012-07-13 00:30:02
41 	Esperanto 	Esperanto 	eo 	26327 	36434 	156071 	5 	2378 	22 	27 	2012-07-13 00:30:31
29 	Spanish 	Español 	es 	71934 	99329 	807921 	16 	39689 	87 	10 	2012-07-13 00:30:13
27 	Estonian 	Eesti 	et 	95136 	109791 	551280 	6 	2764 	17 	2 	2012-07-13 00:30:08
39 	Basque 	Euskara 	eu 	31839 	37701 	129411 	3 	1461 	16 	3 	2012-07-13 00:30:35
30 	Persian 	فارسی 	fa 	58151 	108947 	664867 	3 	6474 	25 	4 	2012-07-13 00:30:16
18 	Finnish 	Suomi 	fi 	196230 	276133 	1601457 	11 	9074 	41 	14 	2012-07-13 00:30:05
58 	Fijian 	Na Vosa Vakaviti 	fj 	10884 	11477 	60006 	0 	871 	11 	0 	2012-07-13 00:30:50
110 	Faroese 	Føroyskt 	fo 	333 	1172 	12577 	2 	994 	9 	0 	2012-07-13 00:30:41
2 	French 	Français 	fr 	2214192 	2364851 	11095925 	24 	71837 	350 	30 	2012-07-13 00:30:02
55 	West Frisian 	Frysk 	fy 	13323 	17342 	90344 	0 	1314 	11 	13 	2012-07-13 00:30:44
85 	Irish 	Gaeilge 	ga 	2262 	5072 	37773 	3 	2093 	11 	32 	2012-07-13 00:30:36
125 	Scottish Gaelic 	Gàidhlig 	gd 	174 	986 	8023 	0 	929 	7 	0 	2012-07-13 00:31:14
36 	Galician 	Galego 	gl 	35582 	43372 	448033 	3 	2270 	17 	74 	2012-07-13 00:30:06
89 	Guarani 	Avañe'ẽ 	gn 	1701 	2281 	11655 	0 	926 	11 	1 	2012-07-13 00:30:51
107 	Gujarati 	ગુજરાતી 	gu 	411 	2431 	15150 	2 	1214 	10 	9 	2012-07-13 00:30:26
115 	Manx 	Gaelg 	gv 	277 	1094 	8384 	0 	800 	7 	1 	2012-07-13 00:31:15
144 	Hausa 	هَوُسَ 	ha 	54 	541 	6087 	0 	754 	8 	0 	2012-07-13 00:31:16
60 	Hebrew 	עברית 	he 	10104 	21883 	150069 	5 	7616 	44 	216 	2012-07-13 00:30:15
69 	Hindi 	हिन्दी 	hi 	6194 	120776 	154205 	4 	3810 	16 	19 	2012-07-13 00:30:17
46 	Croatian 	Hrvatski 	hr 	21850 	29150 	129266 	7 	2428 	17 	5 	2012-07-13 00:30:24
77 	Upper Sorbian 	Obersorbisch 	hsb 	4051 	5035 	10342 	1 	1012 	12 	0 	2012-07-13 00:31:46
19 	Hungarian 	Magyar 	hu 	183902 	203729 	1330633 	5 	8886 	24 	1 	2012-07-13 00:30:08
65 	Armenian 	Հայերեն 	hy 	7237 	9923 	98525 	2 	1486 	13 	16 	2012-07-13 00:30:40
95 	Interlingua 	Interlingua 	ia 	1159 	7331 	24684 	0 	1342 	16 	5 	2012-07-13 00:30:28
24 	Indonesian 	Bahasa Indonesia 	id 	112173 	183266 	618177 	5 	5878 	21 	1 	2012-07-13 00:30:28
113 	Interlingue 	Interlingue 	ie 	283 	2150 	3997 	0 	879 	6 	0 	2012-07-13 00:30:32
131 	Inupiak 	Iñupiak 	ik 	134 	645 	6728 	0 	780 	10 	0 	2012-07-13 00:31:38
17 	Ido 	Ido 	io 	198016 	227214 	1655024 	2 	3045 	20 	1 	2012-07-13 00:30:03
47 	Icelandic 	Íslenska 	is 	21116 	25740 	187530 	7 	2584 	14 	4 	2012-07-13 00:30:19
23 	Italian 	Italiano 	it 	118202 	137799 	1342334 	10 	25272 	66 	41 	2012-07-13 00:30:03
118 	Inuktitut 	ᐃᓄᒃᑎᑐᑦ 	iu 	249 	807 	7955 	0 	850 	10 	0 	2012-07-13 00:31:17
28 	Japanese 	日本語 	ja 	80271 	96367 	542083 	8 	18930 	41 	43 	2012-07-13 00:30:10
126 	Lojban 	Lojban 	jbo 	170 	726 	5009 	0 	784 	12 	0 	2012-07-13 00:31:39
129 	Javanese 	Basa Jawa 	jv 	148 	869 	7963 	0 	825 	9 	0 	2012-07-13 00:30:54
72 	Georgian 	ქართული 	ka 	5462 	8357 	47031 	2 	1701 	13 	12 	2012-07-13 00:30:38
78 	Kazakh 	қазақша 	kk 	3737 	5289 	62444 	1 	1121 	11 	8 	2012-07-13 00:30:27
102 	Greenlandic 	Kalaallisut 	kl 	838 	2295 	16154 	0 	981 	10 	2 	2012-07-13 00:31:17
81 	Cambodian 	ភាសាខ្មែរ 	km 	2552 	21123 	36093 	0 	1000 	12 	80 	2012-07-13 00:30:51
15 	Kannada 	ಕನ್ನಡ 	kn 	209574 	212385 	403249 	1 	1581 	15 	0 	2012-07-13 00:30:37
8 	Korean 	한국어 	ko 	343346 	357810 	1314310 	1 	5296 	35 	25 	2012-07-13 00:30:14
145 	Kashmiri 	कश्मीरी - (كشميري 	ks 	48 	630 	5949 	0 	690 	8 	0 	2012-07-13 00:30:48
31 	Kurdish 	Kurdî / كوردی 	ku 	54087 	60529 	282582 	4 	2061 	28 	40 	2012-07-13 00:30:06
111 	Cornish 	Kernewek/Karnuack 	kw 	302 	820 	8334 	0 	746 	10 	0 	2012-07-13 00:31:06
101 	Kirghiz 	Kırgızca 	ky 	859 	3958 	28688 	0 	942 	7 	1 	2012-07-13 00:31:03
61 	Latin 	Latina 	la 	8279 	18250 	98151 	2 	4076 	16 	29 	2012-07-13 00:30:13
75 	Luxembourgish 	Lëtzebuergesch 	lb 	5270 	9272 	41891 	2 	1019 	12 	178 	2012-07-13 00:30:49
25 	Limburgian 	Limburgs 	li 	108424 	113657 	385048 	3 	1666 	9 	13 	2012-07-13 00:31:39
133 	Lingala 	Lingala 	ln 	127 	650 	6424 	0 	709 	10 	0 	2012-07-13 00:31:18
34 	Lao 	ລາວ 	lo 	37602 	62040 	284246 	0 	1474 	9 	2 	2012-07-13 00:31:40
5 	Lithuanian 	Lietuvių 	lt 	608275 	679649 	1385862 	4 	3662 	22 	27 	2012-07-13 00:30:34
71 	Latvian 	Latviešu 	lv 	5753 	7867 	35866 	1 	1240 	14 	9 	2012-07-13 00:30:40
3 	Malagasy 	Malagasy 	mg 	1532895 	1539902 	8431012 	2 	1536 	16 	2 	2012-07-13 00:30:42
171 	Marshallese 	Ebon 	mh 	0 	41 	3357 	0 	312 	0 	0 	2012-07-13 00:31:40
128 	Maori 	Māori 	mi 	149 	800 	7109 	2 	833 	11 	0 	2012-07-13 00:30:46
82 	Macedonian 	Македонски 	mk 	2521 	4748 	11800 	2 	1121 	8 	0 	2012-07-13 00:31:02
26 	Malayalam 	മലയാളം 	ml 	102547 	108678 	509886 	5 	2173 	21 	196 	2012-07-13 00:30:26
106 	Mongolian 	Монгол 	mn 	443 	1270 	9643 	0 	953 	7 	1 	2012-07-13 00:31:06
149 	Moldovan 	Moldoveana 	mo 	5 	215 	4945 	0 	362 	0 	0 	2012-07-13 00:31:19
90 	Marathi 	मराठी 	mr 	1664 	4555 	10992 	2 	1270 	9 	4 	2012-07-13 00:31:19
87 	Malay 	Bahasa Melayu 	ms 	2103 	6359 	84901 	2 	1764 	12 	3 	2012-07-13 00:30:45
127 	Maltese 	bil-Malti 	mt 	163 	911 	7305 	1 	806 	6 	2 	2012-07-13 00:30:54
22 	Burmese 	Myanmasa 	my 	122872 	124458 	440199 	1 	1804 	11 	2 	2012-07-13 00:31:20
139 	Nauruan 	dorerin Naoero 	na 	106 	779 	7048 	1 	788 	7 	0 	2012-07-13 00:30:44
80 	Nahuatl 	Nahuatl 	nah 	3048 	4448 	26366 	3 	858 	10 	1 	2012-07-13 00:31:00
94 	Low Saxon 	Plattdüütsch 	nds 	1256 	3406 	14861 	3 	1035 	10 	14 	2012-07-13 00:30:30
143 	Nepali 	नेपाली 	ne 	75 	5709 	14054 	2 	1005 	8 	27 	2012-07-13 00:30:43
16 	Dutch 	Nederlands 	nl 	204348 	228936 	1418589 	11 	12169 	57 	12 	2012-07-13 00:30:05
73 	Norwegian (Nynorsk) 	Nynorsk 	nn 	5424 	6446 	17993 	3 	1085 	9 	4 	2012-07-13 00:30:46
21 	Norwegian (Bokmål) 	Norsk (Bokmål) 	no 	127981 	137164 	577825 	10 	4070 	24 	10 	2012-07-13 00:30:11
45 	Occitan 	Occitan 	oc 	22332 	26061 	157970 	3 	1500 	16 	3 	2012-07-13 00:30:36
116 	Oromo 	Oromoo 	om 	268 	792 	8672 	0 	757 	7 	0 	2012-07-13 00:31:41
134 	Oriya 	ଓଡ଼ିଆ 	or 	118 	1130 	6513 	1 	598 	7 	1 	2012-07-13 00:31:22
140 	Panjabi 	ਪੰਜਾਬੀ 	pa 	105 	735 	7655 	0 	783 	6 	0 	2012-07-13 00:31:07
151 	Pali 	पािऴ 	pi 	1 	34 	3311 	0 	348 	0 	0 	2012-07-13 00:31:42
10 	Polish 	Polski 	pl 	289910 	310134 	2961712 	26 	26013 	89 	38 	2012-07-13 00:30:02
74 	Western Panjabi 	شاہ مکھی پنجابی (Shāhmukhī Pañj� 	pnb 	5367 	6157 	14397 	1 	241 	11 	0 	2012-07-13 00:31:46
109 	Pashto 	پښتو 	ps 	354 	10094 	23010 	1 	797 	17 	4 	2012-07-13 00:31:08
20 	Portuguese 	Português 	pt 	180390 	261781 	1487606 	7 	31616 	74 	1 	2012-07-13 00:30:07
114 	Quechua 	Runa Simi 	qu 	281 	1386 	10713 	2 	809 	8 	2 	2012-07-13 00:30:57
162 	Raeto Romance 	Rumantsch 	rm 	0 	102 	4775 	0 	349 	0 	0 	2012-07-13 00:31:23
166 	Kirundi 	Kirundi 	rn 	0 	54 	4695 	0 	388 	0 	0 	2012-07-13 00:31:42
33 	Romanian 	Română 	ro 	46887 	60531 	392181 	7 	6566 	14 	11 	2012-07-13 00:30:29
117 	Aromanian 	Armãneashce 	roa-rup 	252 	782 	8779 	0 	790 	7 	0 	2012-07-13 00:31:42
6 	Russian 	Русский 	ru 	440674 	637098 	3265154 	7 	68247 	163 	276 	2012-07-13 00:30:14
108 	Rwandi 	Kinyarwanda 	rw 	357 	1055 	9487 	0 	784 	8 	0 	2012-07-13 00:31:08
105 	Sanskrit 	संस्कृतम् 	sa 	482 	1315 	10293 	0 	1094 	16 	4 	2012-07-13 00:30:57
164 	Sardinian 	Sardu 	sc 	0 	62 	5983 	0 	376 	0 	0 	2012-07-13 00:31:09
50 	Sicilian 	Sicilianu 	scn 	17042 	23496 	121237 	6 	1606 	15 	26 	2012-07-13 00:30:09
98 	Sindhi 	سنڌي، سندھی ، सिन्ध 	sd 	963 	2737 	15805 	1 	787 	7 	43 	2012-07-13 00:31:23
138 	Sango 	Sängö 	sg 	107 	643 	6373 	0 	710 	8 	1 	2012-07-13 00:31:43
97 	Serbo-Croatian 	Srpskohrvatski / Српскохрватски 	sh 	1027 	1891 	11821 	6 	992 	8 	3 	2012-07-13 00:31:43
96 	Sinhalese 	සිංහල 	si 	1066 	2470 	11962 	0 	909 	10 	10 	2012-07-13 00:31:44
49 	Simple English 	Simple English 	simple 	18684 	24781 	234646 	8 	7215 	38 	2 	2012-07-13 00:30:31
92 	Slovak 	Slovenčina 	sk 	1462 	4580 	22052 	6 	1935 	9 	0 	2012-07-13 00:30:20
67 	Slovenian 	Slovenščina 	sl 	6981 	15725 	87907 	3 	2661 	11 	0 	2012-07-13 00:30:10
136 	Samoan 	Gagana Samoa 	sm 	112 	630 	6493 	0 	707 	6 	0 	2012-07-13 00:30:58
156 	Shona 	chiShona 	sn 	0 	205 	4905 	0 	295 	0 	0 	2012-07-13 00:31:24
122 	Somali 	Soomaaliga 	so 	200 	906 	8344 	0 	755 	7 	0 	2012-07-13 00:31:44
63 	Albanian 	Shqip 	sq 	7314 	10340 	40737 	3 	1712 	11 	3 	2012-07-13 00:30:40
51 	Serbian 	Српски / Srpski 	sr 	15951 	19571 	85408 	5 	2765 	12 	7 	2012-07-13 00:30:30
112 	Swati 	SiSwati 	ss 	290 	1181 	7466 	2 	725 	8 	0 	2012-07-13 00:31:24
93 	Southern Sotho 	seSotho 	st 	1343 	3002 	6522 	1 	1218 	7 	2 	2012-07-13 00:31:25
123 	Sundanese 	Basa Sunda 	su 	182 	1052 	7868 	1 	777 	6 	0 	2012-07-13 00:30:45
12 	Swedish 	Svenska 	sv 	247291 	264323 	1287919 	19 	10722 	59 	1 	2012-07-13 00:30:09
53 	Swahili 	Kiswahili 	sw 	13863 	16691 	84970 	1 	1327 	12 	0 	2012-07-13 00:30:38
11 	Tamil 	தமிழ் 	ta 	256452 	264810 	1135871 	12 	4635 	46 	225 	2012-07-13 00:30:18
42 	Telugu 	తెలుగు 	te 	23427 	59545 	317848 	3 	1794 	19 	460 	2012-07-13 00:30:43
99 	Tajik 	Тоҷикӣ 	tg 	878 	1772 	10720 	1 	769 	6 	0 	2012-07-13 00:30:39
48 	Thai 	ไทย 	th 	20371 	27444 	118535 	3 	2411 	18 	34 	2012-07-13 00:30:34
137 	Tigrinya 	ትግርኛ 	ti 	111 	1061 	7180 	0 	747 	6 	0 	2012-07-13 00:31:45
76 	Turkmen 	تركمن / Туркмен 	tk 	4405 	5164 	17957 	1 	901 	8 	0 	2012-07-13 00:31:38
56 	Tagalog 	Tagalog 	tl 	12662 	23103 	82863 	1 	1328 	16 	17 	2012-07-13 00:31:02
141 	Setswana 	Setswana 	tn 	94 	618 	6170 	1 	681 	6 	0 	2012-07-13 00:31:25
158 	Tongan 	faka Tonga 	to 	0 	199 	4858 	0 	332 	0 	0 	2012-07-13 00:31:45
130 	Tok Pisin 	Tok Pisin 	tpi 	146 	789 	8058 	0 	775 	8 	0 	2012-07-13 00:30:47
9 	Turkish 	Türkçe 	tr 	308708 	331297 	1178972 	3 	17630 	47 	1 	2012-07-13 00:30:18
147 	Tsonga 	Xitsonga 	ts 	19 	817 	6434 	0 	742 	7 	0 	2012-07-13 00:31:26
68 	Tatar 	Tatarça / Татарча 	tt 	6262 	8515 	23988 	1 	1011 	11 	1 	2012-07-13 00:31:05
157 	Twi 	Twi 	tw 	0 	202 	4807 	0 	388 	0 	0 	2012-07-13 00:31:26
100 	Uyghur 	Oyghurque 	ug 	861 	1513 	15584 	0 	831 	5 	0 	2012-07-13 00:31:33
38 	Ukrainian 	Українська 	uk 	32543 	40701 	162096 	5 	6205 	24 	15 	2012-07-13 00:30:27
59 	Urdu 	اردو 	ur 	10213 	15893 	27137 	1 	1321 	11 	2 	2012-07-13 00:30:41
135 	Uzbek 	O‘zbek 	uz 	115 	644 	6974 	0 	767 	7 	0 	2012-07-13 00:30:50
13 	Vietnamese 	Tiếng Việt 	vi 	229340 	240529 	1478531 	6 	14607 	40 	6 	2012-07-13 00:30:25
43 	Volapük 	Volapük 	vo 	22488 	24693 	154283 	3 	1189 	11 	1 	2012-07-13 00:30:35
62 	Walloon 	Walon 	wa 	7455 	35221 	103228 	1 	1035 	18 	16 	2012-07-13 00:31:35
84 	Wolof 	Wollof 	wo 	2309 	3686 	17178 	1 	878 	6 	5 	2012-07-13 00:31:27
163 	Xhosan 	isiXhosa 	xh 	0 	72 	5211 	0 	364 	0 	0 	2012-07-13 00:31:27
132 	Yiddish 	ייִדיש 	yi 	128 	2447 	13787 	45 	2756 	10 	24 	2012-07-13 00:30:33
160 	Yoruba 	Yorùbá 	yo 	0 	185 	4921 	0 	325 	0 	0 	2012-07-13 00:31:28
124 	Zhuang 	Cuengh 	za 	179 	611 	12639 	0 	738 	7 	0 	2012-07-13 00:31:37
4 	Chinese 	中文 	zh 	827299 	1322383 	3734945 	8 	23767 	58 	195 	2012-07-13 00:30:04
66 	Min Nan 	Bân-lâm-gú 	zh-min-nan 	7066 	9518 	73522 	4 	1252 	9 	1 	2012-07-13 00:31:29
104 	Zulu 	isiZulu 	zu 	579 	1253 	8581 	0 	914 	6 	0 	2012-07-13 00:31:28
 */
	static {
		langIDs = new HashMap<String,LanguageID>();
		langIDs_Desc = new HashMap<String,LanguageID>();
		
		LanguageID lid = null;
		
		lid = new LanguageID(LANG_AA, LANG_AA_STR, LANG_AA_ABR);
		langIDs.put(LANG_AA, lid);
		langIDs_Desc.put(LANG_AA_STR, lid);
		
		lid = new LanguageID(LANG_AB, LANG_AB_STR, LANG_AB_ABR);
		langIDs.put(LANG_AB, lid);
		langIDs_Desc.put(LANG_AB_STR, lid);
		
		lid = new LanguageID(LANG_AF, LANG_AF_STR, LANG_AF_ABR);
		langIDs.put(LANG_AF, lid);
		langIDs_Desc.put(LANG_AF_STR, lid);
		
		lid = new LanguageID(LANG_AK, LANG_AK_STR, LANG_AK_ABR);
		langIDs.put(LANG_AK, lid);
		langIDs_Desc.put(LANG_AK_STR, lid);
		
		lid = new LanguageID(LANG_ALS, LANG_ALS_STR, LANG_ALS_ABR);
		langIDs.put(LANG_ALS, lid);
		langIDs_Desc.put(LANG_ALS_STR, lid);
		
		lid = new LanguageID(LANG_AM, LANG_AM_STR, LANG_AM_ABR);
		langIDs.put(LANG_AM, lid);
		langIDs_Desc.put(LANG_AM_STR, lid);
		
		lid = new LanguageID(LANG_AN, LANG_AN_STR, LANG_AN_ABR);
		langIDs.put(LANG_AN, lid);
		langIDs_Desc.put(LANG_AN_STR, lid);
		
		lid = new LanguageID(LANG_ANG, LANG_ANG_STR, LANG_ANG_ABR);
		langIDs.put(LANG_ANG, lid);
		langIDs_Desc.put(LANG_ANG_STR, lid);
		
		lid = new LanguageID(LANG_AR, LANG_AR_STR, LANG_AR_ABR);
		langIDs.put(LANG_AR, lid);
		langIDs_Desc.put(LANG_AR_STR, lid);
		
		lid = new LanguageID(LANG_AS, LANG_AS_STR, LANG_AS_ABR);
		langIDs.put(LANG_AS, lid);
		langIDs_Desc.put(LANG_AS_STR, lid);
		
		lid = new LanguageID(LANG_AST, LANG_AST_STR, LANG_AST_ABR);
		langIDs.put(LANG_AST, lid);
		langIDs_Desc.put(LANG_AST_STR, lid);
		
		lid = new LanguageID(LANG_AV, LANG_AV_STR, LANG_AV_ABR);
		langIDs.put(LANG_AV, lid);
		langIDs_Desc.put(LANG_AV_STR, lid);
		
		lid = new LanguageID(LANG_AY, LANG_AY_STR, LANG_AY_ABR);
		langIDs.put(LANG_AY, lid);
		langIDs_Desc.put(LANG_AY_STR, lid);
		
		lid = new LanguageID(LANG_AZ, LANG_AZ_STR, LANG_AZ_ABR);
		langIDs.put(LANG_AZ, lid);
		langIDs_Desc.put(LANG_AZ_STR, lid);
		
		lid = new LanguageID(LANG_BE, LANG_BE_STR, LANG_BE_ABR);
		langIDs.put(LANG_BE, lid);
		langIDs_Desc.put(LANG_BE_STR, lid);
		
		lid = new LanguageID(LANG_BG, LANG_BG_STR, LANG_BG_ABR);
		langIDs.put(LANG_BG, lid);
		langIDs_Desc.put(LANG_BG_STR, lid);
		
		lid = new LanguageID(LANG_BH, LANG_BH_STR, LANG_BH_ABR);
		langIDs.put(LANG_BH, lid);
		langIDs_Desc.put(LANG_BH_STR, lid);
		
		lid = new LanguageID(LANG_BI, LANG_BI_STR, LANG_BI_ABR);
		langIDs.put(LANG_BI, lid);
		langIDs_Desc.put(LANG_BI_STR, lid);
		
		lid = new LanguageID(LANG_BM, LANG_BM_STR, LANG_BM_ABR);
		langIDs.put(LANG_BM, lid);
		langIDs_Desc.put(LANG_BM_STR, lid);
		
		lid = new LanguageID(LANG_BN, LANG_BN_STR, LANG_BN_ABR);
		langIDs.put(LANG_BN, lid);
		langIDs_Desc.put(LANG_BN_STR, lid);
		
		lid = new LanguageID(LANG_BO, LANG_BO_STR, LANG_BO_ABR);
		langIDs.put(LANG_BO, lid);
		langIDs_Desc.put(LANG_BO_STR, lid);
		
		lid = new LanguageID(LANG_BR, LANG_BR_STR, LANG_BR_ABR);
		langIDs.put(LANG_BR, lid);
		langIDs_Desc.put(LANG_BR_STR, lid);
		
		lid = new LanguageID(LANG_BS, LANG_BS_STR, LANG_BS_ABR);
		langIDs.put(LANG_BS, lid);
		langIDs_Desc.put(LANG_BS_STR, lid);
		
		lid = new LanguageID(LANG_CA, LANG_CA_STR, LANG_CA_ABR);
		langIDs.put(LANG_CA, lid);
		langIDs_Desc.put(LANG_CA_STR, lid);
		
		lid = new LanguageID(LANG_CHA, LANG_CHA_STR, LANG_CHA_ABR);
		langIDs.put(LANG_CHA, lid);
		langIDs_Desc.put(LANG_CHA_STR, lid);
		
		lid = new LanguageID(LANG_CHR, LANG_CHR_STR, LANG_CHR_ABR);
		langIDs.put(LANG_CHR, lid);
		langIDs_Desc.put(LANG_CHR_STR, lid);
		
		lid = new LanguageID(LANG_CO, LANG_CO_STR, LANG_CO_ABR);
		langIDs.put(LANG_CO, lid);
		langIDs_Desc.put(LANG_CO_STR, lid);
		
		lid = new LanguageID(LANG_CR, LANG_CR_STR, LANG_CR_ABR);
		langIDs.put(LANG_CR, lid);
		langIDs_Desc.put(LANG_CR_STR, lid);
		
		lid = new LanguageID(LANG_CS, LANG_CS_STR, LANG_CS_ABR);
		langIDs.put(LANG_CS, lid);
		langIDs_Desc.put(LANG_CS_STR, lid);
		
		lid = new LanguageID(LANG_CSB, LANG_CSB_STR, LANG_CSB_ABR);
		langIDs.put(LANG_CSB, lid);
		langIDs_Desc.put(LANG_CSB_STR, lid);
		
		lid = new LanguageID(LANG_CY, LANG_CY_STR, LANG_CY_ABR);
		langIDs.put(LANG_CY, lid);
		langIDs_Desc.put(LANG_CY_STR, lid);
		
		lid = new LanguageID(LANG_DA, LANG_DA_STR, LANG_DA_ABR);
		langIDs.put(LANG_DA, lid);
		langIDs_Desc.put(LANG_DA_STR, lid);
		
		lid = new LanguageID(LANG_DE, LANG_DE_STR, LANG_DE_ABR);
		langIDs.put(LANG_DE, lid);
		langIDs_Desc.put(LANG_DE_STR, lid);
		
		lid = new LanguageID(LANG_DI, LANG_DI_STR, LANG_DI_ABR);
		langIDs.put(LANG_DI, lid);
		langIDs_Desc.put(LANG_DI_STR, lid);
		
		lid = new LanguageID(LANG_DZ, LANG_DZ_STR, LANG_DZ_ABR);
		langIDs.put(LANG_DZ, lid);
		langIDs_Desc.put(LANG_DZ_STR, lid);
		
		lid = new LanguageID(LANG_EL, LANG_EL_STR, LANG_EL_ABR);
		langIDs.put(LANG_EL, lid);
		langIDs_Desc.put(LANG_EL_STR, lid);
		
		lid = new LanguageID(LANG_EN, LANG_EN_STR, LANG_EN_ABR);
		langIDs.put(LANG_EN, lid);
		langIDs_Desc.put(LANG_EN_STR, lid);
		
		lid = new LanguageID(LANG_EO, LANG_EO_STR, LANG_EO_ABR);
		langIDs.put(LANG_EO, lid);
		langIDs_Desc.put(LANG_EO_STR, lid);
		
		lid = new LanguageID(LANG_ES, LANG_ES_STR, LANG_ES_ABR);
		langIDs.put(LANG_ES, lid);
		langIDs_Desc.put(LANG_ES_STR, lid);
		
		lid = new LanguageID(LANG_ET, LANG_ET_STR, LANG_ET_ABR);
		langIDs.put(LANG_ET, lid);
		langIDs_Desc.put(LANG_ET_STR, lid);
		
		lid = new LanguageID(LANG_EU, LANG_EU_STR, LANG_EU_ABR);
		langIDs.put(LANG_EU, lid);
		langIDs_Desc.put(LANG_EU_STR, lid);
		
		lid = new LanguageID(LANG_FA, LANG_FA_STR, LANG_FA_ABR);
		langIDs.put(LANG_FA, lid);
		langIDs_Desc.put(LANG_FA_STR, lid);
		
		lid = new LanguageID(LANG_FI, LANG_FI_STR, LANG_FI_ABR);
		langIDs.put(LANG_FI, lid);
		langIDs_Desc.put(LANG_FI_STR, lid);
		
		lid = new LanguageID(LANG_FJ, LANG_FJ_STR, LANG_FJ_ABR);
		langIDs.put(LANG_FJ, lid);
		langIDs_Desc.put(LANG_FJ_STR, lid);
		
		lid = new LanguageID(LANG_FO, LANG_FO_STR, LANG_FO_ABR);
		langIDs.put(LANG_FO, lid);
		langIDs_Desc.put(LANG_FO_STR, lid);
		
		lid = new LanguageID(LANG_FR, LANG_FR_STR, LANG_FR_ABR);
		langIDs.put(LANG_FR, lid);
		langIDs_Desc.put(LANG_FR_STR, lid);
		
		lid = new LanguageID(LANG_FY, LANG_FY_STR, LANG_FY_ABR);
		langIDs.put(LANG_FY, lid);
		langIDs_Desc.put(LANG_FY_STR, lid);
		
		lid = new LanguageID(LANG_GD, LANG_GD_STR, LANG_GD_ABR);
		langIDs.put(LANG_GD, lid);
		langIDs_Desc.put(LANG_GD_STR, lid);
		
		lid = new LanguageID(LANG_GL, LANG_GL_STR, LANG_GL_ABR);
		langIDs.put(LANG_GL, lid);
		langIDs_Desc.put(LANG_GL_STR, lid);
		
		lid = new LanguageID(LANG_GN, LANG_GN_STR, LANG_GN_ABR);
		langIDs.put(LANG_GN, lid);
		langIDs_Desc.put(LANG_GN_STR, lid);
		
		lid = new LanguageID(LANG_GU, LANG_GU_STR, LANG_GU_ABR);
		langIDs.put(LANG_GU, lid);
		langIDs_Desc.put(LANG_GU_STR, lid);
		
		lid = new LanguageID(LANG_GV, LANG_GV_STR, LANG_GV_ABR);
		langIDs.put(LANG_GV, lid);
		langIDs_Desc.put(LANG_GV_STR, lid);
		
		lid = new LanguageID(LANG_HA, LANG_HA_STR, LANG_HA_ABR);
		langIDs.put(LANG_HA, lid);
		langIDs_Desc.put(LANG_HA_STR, lid);
		
		lid = new LanguageID(LANG_HE, LANG_HE_STR, LANG_HE_ABR);
		langIDs.put(LANG_HE, lid);
		langIDs_Desc.put(LANG_HE_STR, lid);
		
		lid = new LanguageID(LANG_HI, LANG_HI_STR, LANG_HI_ABR);
		langIDs.put(LANG_HI, lid);
		langIDs_Desc.put(LANG_HI_STR, lid);
		
		lid = new LanguageID(LANG_HR, LANG_HR_STR, LANG_HR_ABR);
		langIDs.put(LANG_HR, lid);
		langIDs_Desc.put(LANG_HR_STR, lid);
		
		lid = new LanguageID(LANG_HSB, LANG_HSB_STR, LANG_HSB_ABR);
		langIDs.put(LANG_HSB, lid);
		langIDs_Desc.put(LANG_HSB_STR, lid);
		
		lid = new LanguageID(LANG_HU, LANG_HU_STR, LANG_HU_ABR);
		langIDs.put(LANG_HU, lid);
		langIDs_Desc.put(LANG_HU_STR, lid);
		
		lid = new LanguageID(LANG_HY, LANG_HY_STR, LANG_HY_ABR);
		langIDs.put(LANG_HY, lid);
		langIDs_Desc.put(LANG_HY_STR, lid);
		
		lid = new LanguageID(LANG_IA, LANG_IA_STR, LANG_IA_ABR);
		langIDs.put(LANG_IA, lid);
		langIDs_Desc.put(LANG_IA_STR, lid);
		
		lid = new LanguageID(LANG_ID, LANG_ID_STR, LANG_ID_ABR);
		langIDs.put(LANG_ID, lid);
		langIDs_Desc.put(LANG_ID_STR, lid);
		
		lid = new LanguageID(LANG_IE, LANG_IE_STR, LANG_IE_ABR);
		langIDs.put(LANG_IE, lid);
		langIDs_Desc.put(LANG_IE_STR, lid);
		
		lid = new LanguageID(LANG_IK, LANG_IK_STR, LANG_IK_ABR);
		langIDs.put(LANG_IK, lid);
		langIDs_Desc.put(LANG_IK_STR, lid);
		
		lid = new LanguageID(LANG_IO, LANG_IO_STR, LANG_IO_ABR);
		langIDs.put(LANG_IO, lid);
		langIDs_Desc.put(LANG_IO_STR, lid);
		
		lid = new LanguageID(LANG_IRI, LANG_IRI_STR, LANG_IRI_ABR);
		langIDs.put(LANG_IRI, lid);
		langIDs_Desc.put(LANG_IRI_STR, lid);
		
		lid = new LanguageID(LANG_IS, LANG_IS_STR, LANG_IS_ABR);
		langIDs.put(LANG_IS, lid);
		langIDs_Desc.put(LANG_IS_STR, lid);
		
		lid = new LanguageID(LANG_IT, LANG_IT_STR, LANG_IT_ABR);
		langIDs.put(LANG_IT, lid);
		langIDs_Desc.put(LANG_IT_STR, lid);
		
		lid = new LanguageID(LANG_IU, LANG_IU_STR, LANG_IU_ABR);
		langIDs.put(LANG_IU, lid);
		langIDs_Desc.put(LANG_IU_STR, lid);
		
		lid = new LanguageID(LANG_JA, LANG_JA_STR, LANG_JA_ABR);
		langIDs.put(LANG_JA, lid);
		langIDs_Desc.put(LANG_JA_STR, lid);
		
		lid = new LanguageID(LANG_JBO, LANG_JBO_STR, LANG_JBO_ABR);
		langIDs.put(LANG_JBO, lid);
		langIDs_Desc.put(LANG_JBO_STR, lid);
		
		lid = new LanguageID(LANG_JV, LANG_JV_STR, LANG_JV_ABR);
		langIDs.put(LANG_JV, lid);
		langIDs_Desc.put(LANG_JV_STR, lid);
		
		lid = new LanguageID(LANG_KA, LANG_KA_STR, LANG_KA_ABR);
		langIDs.put(LANG_KA, lid);
		langIDs_Desc.put(LANG_KA_STR, lid);
		
		lid = new LanguageID(LANG_KK, LANG_KK_STR, LANG_KK_ABR);
		langIDs.put(LANG_KK, lid);
		langIDs_Desc.put(LANG_KK_STR, lid);
		
		lid = new LanguageID(LANG_KL, LANG_KL_STR, LANG_KL_ABR);
		langIDs.put(LANG_KL, lid);
		langIDs_Desc.put(LANG_KL_STR, lid);
		
		lid = new LanguageID(LANG_KM, LANG_KM_STR, LANG_KM_ABR);
		langIDs.put(LANG_KM, lid);
		langIDs_Desc.put(LANG_KM_STR, lid);
		
		lid = new LanguageID(LANG_KN, LANG_KN_STR, LANG_KN_ABR);
		langIDs.put(LANG_KN, lid);
		langIDs_Desc.put(LANG_KN_STR, lid);
		
		lid = new LanguageID(LANG_KO, LANG_KO_STR, LANG_KO_ABR);
		langIDs.put(LANG_KO, lid);
		langIDs_Desc.put(LANG_KO_STR, lid);
		
		lid = new LanguageID(LANG_KS, LANG_KS_STR, LANG_KS_ABR);
		langIDs.put(LANG_KS, lid);
		langIDs_Desc.put(LANG_KS_STR, lid);
		
		lid = new LanguageID(LANG_KU, LANG_KU_STR, LANG_KU_ABR);
		langIDs.put(LANG_KU, lid);
		langIDs_Desc.put(LANG_KU_STR, lid);
		
		lid = new LanguageID(LANG_KW, LANG_KW_STR, LANG_KW_ABR);
		langIDs.put(LANG_KW, lid);
		langIDs_Desc.put(LANG_KW_STR, lid);
		
		lid = new LanguageID(LANG_KY, LANG_KY_STR, LANG_KY_ABR);
		langIDs.put(LANG_KY, lid);
		langIDs_Desc.put(LANG_KY_STR, lid);
		
		lid = new LanguageID(LANG_LA, LANG_LA_STR, LANG_LA_ABR);
		langIDs.put(LANG_LA, lid);
		langIDs_Desc.put(LANG_LA_STR, lid);
		
		lid = new LanguageID(LANG_LB, LANG_LB_STR, LANG_LB_ABR);
		langIDs.put(LANG_LB, lid);
		langIDs_Desc.put(LANG_LB_STR, lid);
		
		lid = new LanguageID(LANG_LI, LANG_LI_STR, LANG_LI_ABR);
		langIDs.put(LANG_LI, lid);
		langIDs_Desc.put(LANG_LI_STR, lid);
		
		lid = new LanguageID(LANG_LN, LANG_LN_STR, LANG_LN_ABR);
		langIDs.put(LANG_LN, lid);
		langIDs_Desc.put(LANG_LN_STR, lid);
		
		lid = new LanguageID(LANG_LO, LANG_LO_STR, LANG_LO_ABR);
		langIDs.put(LANG_LO, lid);
		langIDs_Desc.put(LANG_LO_STR, lid);
		
		lid = new LanguageID(LANG_LT, LANG_LT_STR, LANG_LT_ABR);
		langIDs.put(LANG_LT, lid);
		langIDs_Desc.put(LANG_LT_STR, lid);
		
		lid = new LanguageID(LANG_LV, LANG_LV_STR, LANG_LV_ABR);
		langIDs.put(LANG_LV, lid);
		langIDs_Desc.put(LANG_LV_STR, lid);
		
		lid = new LanguageID(LANG_MG, LANG_MG_STR, LANG_MG_ABR);
		langIDs.put(LANG_MG, lid);
		langIDs_Desc.put(LANG_MG_STR, lid);
		
		lid = new LanguageID(LANG_MH, LANG_MH_STR, LANG_MH_ABR);
		langIDs.put(LANG_MH, lid);
		langIDs_Desc.put(LANG_MH_STR, lid);
		
		lid = new LanguageID(LANG_MI, LANG_MI_STR, LANG_MI_ABR);
		langIDs.put(LANG_MI, lid);
		langIDs_Desc.put(LANG_MI_STR, lid);
		
		lid = new LanguageID(LANG_MK, LANG_MK_STR, LANG_MK_ABR);
		langIDs.put(LANG_MK, lid);
		langIDs_Desc.put(LANG_MK_STR, lid);
		
		lid = new LanguageID(LANG_ML, LANG_ML_STR, LANG_ML_ABR);
		langIDs.put(LANG_ML, lid);
		langIDs_Desc.put(LANG_ML_STR, lid);
		
		lid = new LanguageID(LANG_MN, LANG_MN_STR, LANG_MN_ABR);
		langIDs.put(LANG_MN, lid);
		langIDs_Desc.put(LANG_MN_STR, lid);
		
		lid = new LanguageID(LANG_MO, LANG_MO_STR, LANG_MO_ABR);
		langIDs.put(LANG_MO, lid);
		langIDs_Desc.put(LANG_MO_STR, lid);
		
		lid = new LanguageID(LANG_MR, LANG_MR_STR, LANG_MR_ABR);
		langIDs.put(LANG_MR, lid);
		langIDs_Desc.put(LANG_MR_STR, lid);
		
		lid = new LanguageID(LANG_MS, LANG_MS_STR, LANG_MS_ABR);
		langIDs.put(LANG_MS, lid);
		langIDs_Desc.put(LANG_MS_STR, lid);
		
		lid = new LanguageID(LANG_MY, LANG_MY_STR, LANG_MY_ABR);
		langIDs.put(LANG_MY, lid);
		langIDs_Desc.put(LANG_MY_STR, lid);
		
		lid = new LanguageID(LANG_NA, LANG_NA_STR, LANG_NA_ABR);
		langIDs.put(LANG_NA, lid);
		langIDs_Desc.put(LANG_NA_STR, lid);
		
		lid = new LanguageID(LANG_NAH, LANG_NAH_STR, LANG_NAH_ABR);
		langIDs.put(LANG_NAH, lid);
		langIDs_Desc.put(LANG_NAH_STR, lid);
		
		lid = new LanguageID(LANG_NB, LANG_NB_STR, LANG_NB_ABR);
		langIDs.put(LANG_NB, lid);
		langIDs_Desc.put(LANG_NB_STR, lid);
		
		lid = new LanguageID(LANG_NDS, LANG_NDS_STR, LANG_NDS_ABR);
		langIDs.put(LANG_NDS, lid);
		langIDs_Desc.put(LANG_NDS_STR, lid);
		
		lid = new LanguageID(LANG_NE, LANG_NE_STR, LANG_NE_ABR);
		langIDs.put(LANG_NE, lid);
		langIDs_Desc.put(LANG_NE_STR, lid);
		
		lid = new LanguageID(LANG_NL, LANG_NL_STR, LANG_NL_ABR);
		langIDs.put(LANG_NL, lid);
		langIDs_Desc.put(LANG_NL_STR, lid);
		
		lid = new LanguageID(LANG_NN, LANG_NN_STR, LANG_NN_ABR);
		langIDs.put(LANG_NN, lid);
		langIDs_Desc.put(LANG_NN_STR, lid);
		
		lid = new LanguageID(LANG_NO, LANG_NO_STR, LANG_NO_ABR);
		langIDs.put(LANG_NO, lid);
		langIDs_Desc.put(LANG_NO_STR, lid);
		
		lid = new LanguageID(LANG_OC, LANG_OC_STR, LANG_OC_ABR);
		langIDs.put(LANG_OC, lid);
		langIDs_Desc.put(LANG_OC_STR, lid);
		
		lid = new LanguageID(LANG_OM, LANG_OM_STR, LANG_OM_ABR);
		langIDs.put(LANG_OM, lid);
		langIDs_Desc.put(LANG_OM_STR, lid);
		
		lid = new LanguageID(LANG_OR, LANG_OR_STR, LANG_OR_ABR);
		langIDs.put(LANG_OR, lid);
		langIDs_Desc.put(LANG_OR_STR, lid);
		
		lid = new LanguageID(LANG_PA, LANG_PA_STR, LANG_PA_ABR);
		langIDs.put(LANG_PA, lid);
		langIDs_Desc.put(LANG_PA_STR, lid);
		
		lid = new LanguageID(LANG_PI, LANG_PI_STR, LANG_PI_ABR);
		langIDs.put(LANG_PI, lid);
		langIDs_Desc.put(LANG_PI_STR, lid);
		
		lid = new LanguageID(LANG_PL, LANG_PL_STR, LANG_PL_ABR);
		langIDs.put(LANG_PL, lid);
		langIDs_Desc.put(LANG_PL_STR, lid);
		
		lid = new LanguageID(LANG_PNB, LANG_PNB_STR, LANG_PNB_ABR);
		langIDs.put(LANG_PNB, lid);
		langIDs_Desc.put(LANG_PNB_STR, lid);
		
		lid = new LanguageID(LANG_PS, LANG_PS_STR, LANG_PS_ABR);
		langIDs.put(LANG_PS, lid);
		langIDs_Desc.put(LANG_PS_STR, lid);
		
		lid = new LanguageID(LANG_PT, LANG_PT_STR, LANG_PT_ABR);
		langIDs.put(LANG_PT, lid);
		langIDs_Desc.put(LANG_PT_STR, lid);
		
		lid = new LanguageID(LANG_QU, LANG_QU_STR, LANG_QU_ABR);
		langIDs.put(LANG_QU, lid);
		langIDs_Desc.put(LANG_QU_STR, lid);
		
		lid = new LanguageID(LANG_RM, LANG_RM_STR, LANG_RM_ABR);
		langIDs.put(LANG_RM, lid);
		langIDs_Desc.put(LANG_RM_STR, lid);
		
		lid = new LanguageID(LANG_RN, LANG_RN_STR, LANG_RN_ABR);
		langIDs.put(LANG_RN, lid);
		langIDs_Desc.put(LANG_RN_STR, lid);
		
		lid = new LanguageID(LANG_RO, LANG_RO_STR, LANG_RO_ABR);
		langIDs.put(LANG_RO, lid);
		langIDs_Desc.put(LANG_RO_STR, lid);
		
		lid = new LanguageID(LANG_ROA_RUP, LANG_ROA_RUP_STR, LANG_ROA_RUP_ABR);
		langIDs.put(LANG_ROA_RUP, lid);
		langIDs_Desc.put(LANG_ROA_RUP_STR, lid);
		
		lid = new LanguageID(LANG_RU, LANG_RU_STR, LANG_RU_ABR);
		langIDs.put(LANG_RU, lid);
		langIDs_Desc.put(LANG_RU_STR, lid);
		
		lid = new LanguageID(LANG_RW, LANG_RW_STR, LANG_RW_ABR);
		langIDs.put(LANG_RW, lid);
		langIDs_Desc.put(LANG_RW_STR, lid);
		
		lid = new LanguageID(LANG_SA, LANG_SA_STR, LANG_SA_ABR);
		langIDs.put(LANG_SA, lid);
		langIDs_Desc.put(LANG_SA_STR, lid);
		
		lid = new LanguageID(LANG_SC, LANG_SC_STR, LANG_SC_ABR);
		langIDs.put(LANG_SC, lid);
		langIDs_Desc.put(LANG_SC_STR, lid);
		
		lid = new LanguageID(LANG_SCN, LANG_SCN_STR, LANG_SCN_ABR);
		langIDs.put(LANG_SCN, lid);
		langIDs_Desc.put(LANG_SCN_STR, lid);
		
		lid = new LanguageID(LANG_SD, LANG_SD_STR, LANG_SD_ABR);
		langIDs.put(LANG_SD, lid);
		langIDs_Desc.put(LANG_SD_STR, lid);
		
		lid = new LanguageID(LANG_SG, LANG_SG_STR, LANG_SG_ABR);
		langIDs.put(LANG_SG, lid);
		langIDs_Desc.put(LANG_SG_STR, lid);
		
		lid = new LanguageID(LANG_SH, LANG_SH_STR, LANG_SH_ABR);
		langIDs.put(LANG_SH, lid);
		langIDs_Desc.put(LANG_SH_STR, lid);
		
		lid = new LanguageID(LANG_SI, LANG_SI_STR, LANG_SI_ABR);
		langIDs.put(LANG_SI, lid);
		langIDs_Desc.put(LANG_SI_STR, lid);
		
		lid = new LanguageID(LANG_SK, LANG_SK_STR, LANG_SK_ABR);
		langIDs.put(LANG_SK, lid);
		langIDs_Desc.put(LANG_SK_STR, lid);
		
		lid = new LanguageID(LANG_SL, LANG_SL_STR, LANG_SL_ABR);
		langIDs.put(LANG_SL, lid);
		langIDs_Desc.put(LANG_SL_STR, lid);
		
		lid = new LanguageID(LANG_SM, LANG_SM_STR, LANG_SM_ABR);
		langIDs.put(LANG_SM, lid);
		langIDs_Desc.put(LANG_SM_STR, lid);
		
		lid = new LanguageID(LANG_SN, LANG_SN_STR, LANG_SN_ABR);
		langIDs.put(LANG_SN, lid);
		langIDs_Desc.put(LANG_SN_STR, lid);
		
		lid = new LanguageID(LANG_SO, LANG_SO_STR, LANG_SO_ABR);
		langIDs.put(LANG_SO, lid);
		langIDs_Desc.put(LANG_SO_STR, lid);
		
		lid = new LanguageID(LANG_SQ, LANG_SQ_STR, LANG_SQ_ABR);
		langIDs.put(LANG_SQ, lid);
		langIDs_Desc.put(LANG_SQ_STR, lid);
		
		lid = new LanguageID(LANG_SR, LANG_SR_STR, LANG_SR_ABR);
		langIDs.put(LANG_SR, lid);
		langIDs_Desc.put(LANG_SR_STR, lid);
		
		lid = new LanguageID(LANG_SS, LANG_SS_STR, LANG_SS_ABR);
		langIDs.put(LANG_SS, lid);
		langIDs_Desc.put(LANG_SS_STR, lid);
		
		lid = new LanguageID(LANG_ST, LANG_ST_STR, LANG_ST_ABR);
		langIDs.put(LANG_ST, lid);
		langIDs_Desc.put(LANG_ST_STR, lid);
		
		lid = new LanguageID(LANG_SU, LANG_SU_STR, LANG_SU_ABR);
		langIDs.put(LANG_SU, lid);
		langIDs_Desc.put(LANG_SU_STR, lid);
		
		lid = new LanguageID(LANG_SV, LANG_SV_STR, LANG_SV_ABR);
		langIDs.put(LANG_SV, lid);
		langIDs_Desc.put(LANG_SV_STR, lid);
		
		lid = new LanguageID(LANG_SW, LANG_SW_STR, LANG_SW_ABR);
		langIDs.put(LANG_SW, lid);
		langIDs_Desc.put(LANG_SW_STR, lid);
		
		lid = new LanguageID(LANG_TA, LANG_TA_STR, LANG_TA_ABR);
		langIDs.put(LANG_TA, lid);
		langIDs_Desc.put(LANG_TA_STR, lid);
		
		lid = new LanguageID(LANG_TE, LANG_TE_STR, LANG_TE_ABR);
		langIDs.put(LANG_TE, lid);
		langIDs_Desc.put(LANG_TE_STR, lid);
		
		lid = new LanguageID(LANG_TG, LANG_TG_STR, LANG_TG_ABR);
		langIDs.put(LANG_TG, lid);
		langIDs_Desc.put(LANG_TG_STR, lid);
		
		lid = new LanguageID(LANG_TH, LANG_TH_STR, LANG_TH_ABR);
		langIDs.put(LANG_TH, lid);
		langIDs_Desc.put(LANG_TH_STR, lid);
		
		lid = new LanguageID(LANG_TI, LANG_TI_STR, LANG_TI_ABR);
		langIDs.put(LANG_TI, lid);
		langIDs_Desc.put(LANG_TI_STR, lid);
		
		lid = new LanguageID(LANG_TK, LANG_TK_STR, LANG_TK_ABR);
		langIDs.put(LANG_TK, lid);
		langIDs_Desc.put(LANG_TK_STR, lid);
		
		lid = new LanguageID(LANG_TL, LANG_TL_STR, LANG_TL_ABR);
		langIDs.put(LANG_TL, lid);
		langIDs_Desc.put(LANG_TL_STR, lid);
		
		lid = new LanguageID(LANG_TN, LANG_TN_STR, LANG_TN_ABR);
		langIDs.put(LANG_TN, lid);
		langIDs_Desc.put(LANG_TN_STR, lid);
		
		lid = new LanguageID(LANG_TO, LANG_TO_STR, LANG_TO_ABR);
		langIDs.put(LANG_TO, lid);
		langIDs_Desc.put(LANG_TO_STR, lid);
		
		lid = new LanguageID(LANG_TPI, LANG_TPI_STR, LANG_TPI_ABR);
		langIDs.put(LANG_TPI, lid);
		langIDs_Desc.put(LANG_TPI_STR, lid);
		
		lid = new LanguageID(LANG_TR, LANG_TR_STR, LANG_TR_ABR);
		langIDs.put(LANG_TR, lid);
		langIDs_Desc.put(LANG_TR_STR, lid);
		
		lid = new LanguageID(LANG_TS, LANG_TS_STR, LANG_TS_ABR);
		langIDs.put(LANG_TS, lid);
		langIDs_Desc.put(LANG_TS_STR, lid);
		
		lid = new LanguageID(LANG_TT, LANG_TT_STR, LANG_TT_ABR);
		langIDs.put(LANG_TT, lid);
		langIDs_Desc.put(LANG_TT_STR, lid);
		
		lid = new LanguageID(LANG_TW, LANG_TW_STR, LANG_TW_ABR);
		langIDs.put(LANG_TW, lid);
		langIDs_Desc.put(LANG_TW_STR, lid);
		
		lid = new LanguageID(LANG_UG, LANG_UG_STR, LANG_UG_ABR);
		langIDs.put(LANG_UG, lid);
		langIDs_Desc.put(LANG_UG_STR, lid);
		
		lid = new LanguageID(LANG_UK, LANG_UK_STR, LANG_UK_ABR);
		langIDs.put(LANG_UK, lid);
		langIDs_Desc.put(LANG_UK_STR, lid);
		
		lid = new LanguageID(LANG_UR, LANG_UR_STR, LANG_UR_ABR);
		langIDs.put(LANG_UR, lid);
		langIDs_Desc.put(LANG_UR_STR, lid);
		
		lid = new LanguageID(LANG_UZ, LANG_UZ_STR, LANG_UZ_ABR);
		langIDs.put(LANG_UZ, lid);
		langIDs_Desc.put(LANG_UZ_STR, lid);
		
		lid = new LanguageID(LANG_VI, LANG_VI_STR, LANG_VI_ABR);
		langIDs.put(LANG_VI, lid);
		langIDs_Desc.put(LANG_VI_STR, lid);
		
		lid = new LanguageID(LANG_VO, LANG_VO_STR, LANG_VO_ABR);
		langIDs.put(LANG_VO, lid);
		langIDs_Desc.put(LANG_VO_STR, lid);
		
		lid = new LanguageID(LANG_WA, LANG_WA_STR, LANG_WA_ABR);
		langIDs.put(LANG_WA, lid);
		langIDs_Desc.put(LANG_WA_STR, lid);
		
		lid = new LanguageID(LANG_WO, LANG_WO_STR, LANG_WO_ABR);
		langIDs.put(LANG_WO, lid);
		langIDs_Desc.put(LANG_WO_STR, lid);
		
		lid = new LanguageID(LANG_XH, LANG_XH_STR, LANG_XH_ABR);
		langIDs.put(LANG_XH, lid);
		langIDs_Desc.put(LANG_XH_STR, lid);
		
		lid = new LanguageID(LANG_YI, LANG_YI_STR, LANG_YI_ABR);
		langIDs.put(LANG_YI, lid);
		langIDs_Desc.put(LANG_YI_STR, lid);
		
		lid = new LanguageID(LANG_YO, LANG_YO_STR, LANG_YO_ABR);
		langIDs.put(LANG_YO, lid);
		langIDs_Desc.put(LANG_YO_STR, lid);
		
		lid = new LanguageID(LANG_ZA, LANG_ZA_STR, LANG_ZA_ABR);
		langIDs.put(LANG_ZA, lid);
		langIDs_Desc.put(LANG_ZA_STR, lid);
		
		lid = new LanguageID(LANG_ZH, LANG_ZH_STR, LANG_ZH_ABR);
		langIDs.put(LANG_ZH, lid);
		langIDs_Desc.put(LANG_ZH_STR, lid);
		
		lid = new LanguageID(LANG_ZH_MIN_NAN, LANG_ZH_MIN_NAN_STR, LANG_ZH_MIN_NAN_ABR);
		langIDs.put(LANG_ZH_MIN_NAN, lid);
		langIDs_Desc.put(LANG_ZH_MIN_NAN_STR, lid);
		
		lid = new LanguageID(LANG_ZU, LANG_ZU_STR, LANG_ZU_ABR);
		langIDs.put(LANG_ZU, lid);
		langIDs_Desc.put(LANG_ZU_STR, lid);
		
		// Extra languages not listed in page of Wiktionaries
		
		// Old langs
		lid = new LanguageID(LANG_AGR, LANG_AGR_STR, LANG_AGR_ABR);
		langIDs.put(LANG_AGR, lid);
		langIDs_Desc.put(LANG_AGR_STR, lid);
		
		lid = new LanguageID(LANG_ANN, LANG_ANN_STR, LANG_ANN_ABR);
		langIDs.put(LANG_ANN, lid);
		langIDs_Desc.put(LANG_ANN_STR, lid);
		
		lid = new LanguageID(LANG_CN, LANG_CN_STR, LANG_CN_ABR);
		langIDs.put(LANG_CN, lid);
		langIDs_Desc.put(LANG_CN_STR, lid);
		
		lid = new LanguageID(LANG_GO, LANG_GO_STR, LANG_GO_ABR);
		langIDs.put(LANG_GO, lid);
		langIDs_Desc.put(LANG_GO_STR, lid);
		
		lid = new LanguageID(LANG_LG, LANG_LG_STR, LANG_LG_ABR);
		langIDs.put(LANG_LG, lid);
		langIDs_Desc.put(LANG_LG_STR, lid);
		
		lid = new LanguageID(LANG_MD, LANG_MD_STR, LANG_MD_ABR);
		langIDs.put(LANG_MD, lid);
		langIDs_Desc.put(LANG_MD_STR, lid);
		
		lid = new LanguageID(LANG_ME, LANG_ME_STR, LANG_ME_ABR);
		langIDs.put(LANG_ME, lid);
		langIDs_Desc.put(LANG_ME_STR, lid);
		
		lid = new LanguageID(LANG_MF, LANG_MF_STR, LANG_MF_ABR);
		langIDs.put(LANG_MF, lid);
		langIDs_Desc.put(LANG_MF_STR, lid);
		
		lid = new LanguageID(LANG_MHG, LANG_MHG_STR, LANG_MHG_ABR);
		langIDs.put(LANG_MHG, lid);
		langIDs_Desc.put(LANG_MHG_STR, lid);
		
		lid = new LanguageID(LANG_MLG, LANG_MLG_STR, LANG_MLG_ABR);
		langIDs.put(LANG_MLG, lid);
		langIDs_Desc.put(LANG_MLG_STR, lid);
		
		lid = new LanguageID(LANG_NMA, LANG_NMA_STR, LANG_NMA_ABR);
		langIDs.put(LANG_NMA, lid);
		langIDs_Desc.put(LANG_NMA_STR, lid);
		
		lid = new LanguageID(LANG_OD, LANG_OD_STR, LANG_OD_ABR);
		langIDs.put(LANG_OD, lid);
		langIDs_Desc.put(LANG_OD_STR, lid);
		
		lid = new LanguageID(LANG_OE, LANG_OE_STR, LANG_OE_ABR);
		langIDs.put(LANG_OE, lid);
		langIDs_Desc.put(LANG_OE_STR, lid);
		
		lid = new LanguageID(LANG_OF, LANG_OF_STR, LANG_OF_ABR);
		langIDs.put(LANG_OF, lid);
		langIDs_Desc.put(LANG_OF_STR, lid);
		
		lid = new LanguageID(LANG_OFI, LANG_OFI_STR, LANG_OFI_ABR);
		langIDs.put(LANG_OFI, lid);
		langIDs_Desc.put(LANG_OFI_STR, lid);
		
		lid = new LanguageID(LANG_OHG, LANG_OHG_STR, LANG_OHG_ABR);
		langIDs.put(LANG_OHG, lid);
		langIDs_Desc.put(LANG_OHG_STR, lid);
		
		lid = new LanguageID(LANG_OI, LANG_OI_STR, LANG_OI_ABR);
		langIDs.put(LANG_OI, lid);
		langIDs_Desc.put(LANG_OI_STR, lid);
		
		lid = new LanguageID(LANG_ON, LANG_ON_STR, LANG_ON_ABR);
		langIDs.put(LANG_ON, lid);
		langIDs_Desc.put(LANG_ON_STR, lid);
		
		lid = new LanguageID(LANG_OPO, LANG_OPO_STR, LANG_OPO_ABR);
		langIDs.put(LANG_OPO, lid);
		langIDs_Desc.put(LANG_OPO_STR, lid);
		
		lid = new LanguageID(LANG_OPR, LANG_OPR_STR, LANG_OPR_ABR);
		langIDs.put(LANG_OPR, lid);
		langIDs_Desc.put(LANG_OPR_STR, lid);
		
		lid = new LanguageID(LANG_OSA, LANG_OSA_STR, LANG_OSA_ABR);
		langIDs.put(LANG_OSA, lid);
		langIDs_Desc.put(LANG_OSA_STR, lid);
		
		lid = new LanguageID(LANG_OSV, LANG_OSV_STR, LANG_OSV_ABR);
		langIDs.put(LANG_OSV, lid);
		langIDs_Desc.put(LANG_OSV_STR, lid);
		
		lid = new LanguageID(LANG_OW, LANG_OW_STR, LANG_OW_ABR);
		langIDs.put(LANG_OW, lid);
		langIDs_Desc.put(LANG_OW_STR, lid);
		
		lid = new LanguageID(LANG_SUME, LANG_SUME_STR, LANG_SUME_ABR);
		langIDs.put(LANG_SUME, lid);
		langIDs_Desc.put(LANG_SUME_STR, lid);
		
		lid = new LanguageID(LANG_AMC, LANG_AMC_STR, LANG_AMC_ABR);
		langIDs.put(LANG_AMC, lid);
		langIDs_Desc.put(LANG_AMC_STR, lid);
		
		lid = new LanguageID(LANG_ANA, LANG_ANA_STR, LANG_ANA_ABR);
		langIDs.put(LANG_ANA, lid);
		langIDs_Desc.put(LANG_ANA_STR, lid);
		
		lid = new LanguageID(LANG_CLS, LANG_CLS_STR, LANG_CLS_ABR);
		langIDs.put(LANG_CLS, lid);
		langIDs_Desc.put(LANG_CLS_STR, lid);
		
		lid = new LanguageID(LANG_CME, LANG_CME_STR, LANG_CME_ABR);
		langIDs.put(LANG_CME, lid);
		langIDs_Desc.put(LANG_CME_STR, lid);
		
		lid = new LanguageID(LANG_DLS, LANG_DLS_STR, LANG_DLS_ABR);
		langIDs.put(LANG_DLS, lid);
		langIDs_Desc.put(LANG_DLS_STR, lid);
		
		lid = new LanguageID(LANG_HIT, LANG_HIT_STR, LANG_HIT_ABR);
		langIDs.put(LANG_HIT, lid);
		langIDs_Desc.put(LANG_HIT_STR, lid);
		
		lid = new LanguageID(LANG_LMC, LANG_LMC_STR, LANG_LMC_ABR);
		langIDs.put(LANG_LMC, lid);
		langIDs_Desc.put(LANG_LMC_STR, lid);
		
		lid = new LanguageID(LANG_MCI, LANG_MCI_STR, LANG_MCI_ABR);
		langIDs.put(LANG_MCI, lid);
		langIDs_Desc.put(LANG_MCI_STR, lid);
		
		lid = new LanguageID(LANG_MIK, LANG_MIK_STR, LANG_MIK_ABR);
		langIDs.put(LANG_MIK, lid);
		langIDs_Desc.put(LANG_MIK_STR, lid);
		
		lid = new LanguageID(LANG_MIP, LANG_MIP_STR, LANG_MIP_ABR);
		langIDs.put(LANG_MIP, lid);
		langIDs_Desc.put(LANG_MIP_STR, lid);
		
		lid = new LanguageID(LANG_MIR, LANG_MIR_STR, LANG_MIR_ABR);
		langIDs.put(LANG_MIR, lid);
		langIDs_Desc.put(LANG_MIR_STR, lid);
		
		lid = new LanguageID(LANG_MIW, LANG_MIW_STR, LANG_MIW_ABR);
		langIDs.put(LANG_MIW, lid);
		langIDs_Desc.put(LANG_MIW_STR, lid);
		
		lid = new LanguageID(LANG_NDA, LANG_NDA_STR, LANG_NDA_ABR);
		langIDs.put(LANG_NDA, lid);
		langIDs_Desc.put(LANG_NDA_STR, lid);
		
		lid = new LanguageID(LANG_NLA, LANG_NLA_STR, LANG_NLA_ABR);
		langIDs.put(LANG_NLA, lid);
		langIDs_Desc.put(LANG_NLA_STR, lid);
		
		lid = new LanguageID(LANG_NOH, LANG_NOH_STR, LANG_NOH_ABR);
		langIDs.put(LANG_NOH, lid);
		langIDs_Desc.put(LANG_NOH_STR, lid);
		
		lid = new LanguageID(LANG_NSO, LANG_NSO_STR, LANG_NSO_ABR);
		langIDs.put(LANG_NSO, lid);
		langIDs_Desc.put(LANG_NSO_STR, lid);
		
		lid = new LanguageID(LANG_NTH, LANG_NTH_STR, LANG_NTH_ABR);
		langIDs.put(LANG_NTH, lid);
		langIDs_Desc.put(LANG_NTH_STR, lid);
		
		lid = new LanguageID(LANG_NYU, LANG_NYU_STR, LANG_NYU_ABR);
		langIDs.put(LANG_NYU, lid);
		langIDs_Desc.put(LANG_NYU_STR, lid);
		
		lid = new LanguageID(LANG_OAR, LANG_OAR_STR, LANG_OAR_ABR);
		langIDs.put(LANG_OAR, lid);
		langIDs_Desc.put(LANG_OAR_STR, lid);
		
		lid = new LanguageID(LANG_OCH, LANG_OCH_STR, LANG_OCH_ABR);
		langIDs.put(LANG_OCH, lid);
		langIDs_Desc.put(LANG_OCH_STR, lid);
		
		lid = new LanguageID(LANG_OCS, LANG_OCS_STR, LANG_OCS_ABR);
		langIDs.put(LANG_OCS, lid);
		langIDs_Desc.put(LANG_OCS_STR, lid);
		
		lid = new LanguageID(LANG_OES, LANG_OES_STR, LANG_OES_ABR);
		langIDs.put(LANG_OES, lid);
		langIDs_Desc.put(LANG_OES_STR, lid);
		
		lid = new LanguageID(LANG_OGE, LANG_OGE_STR, LANG_OGE_ABR);
		langIDs.put(LANG_OGE, lid);
		langIDs_Desc.put(LANG_OGE_STR, lid);
		
		lid = new LanguageID(LANG_OJP, LANG_OJP_STR, LANG_OJP_ABR);
		langIDs.put(LANG_OJP, lid);
		langIDs_Desc.put(LANG_OJP_STR, lid);
		
		lid = new LanguageID(LANG_OJV, LANG_OJV_STR, LANG_OJV_ABR);
		langIDs.put(LANG_OJV, lid);
		langIDs_Desc.put(LANG_OJV_STR, lid);
		
		lid = new LanguageID(LANG_OKO, LANG_OKO_STR, LANG_OKO_ABR);
		langIDs.put(LANG_OKO, lid);
		langIDs_Desc.put(LANG_OKO_STR, lid);
		
		lid = new LanguageID(LANG_OLI, LANG_OLI_STR, LANG_OLI_ABR);
		langIDs.put(LANG_OLI, lid);
		langIDs_Desc.put(LANG_OLI_STR, lid);
		
		lid = new LanguageID(LANG_OPE, LANG_OPE_STR, LANG_OPE_ABR);
		langIDs.put(LANG_OPE, lid);
		langIDs_Desc.put(LANG_OPE_STR, lid);
		
		lid = new LanguageID(LANG_OPL, LANG_OPL_STR, LANG_OPL_ABR);
		langIDs.put(LANG_OPL, lid);
		langIDs_Desc.put(LANG_OPL_STR, lid);
		
		lid = new LanguageID(LANG_OPU, LANG_OPU_STR, LANG_OPU_ABR);
		langIDs.put(LANG_OPU, lid);
		langIDs_Desc.put(LANG_OPU_STR, lid);
		
		lid = new LanguageID(LANG_OSP, LANG_OSP_STR, LANG_OSP_ABR);
		langIDs.put(LANG_OSP, lid);
		langIDs_Desc.put(LANG_OSP_STR, lid);
		
		lid = new LanguageID(LANG_OSR, LANG_OSR_STR, LANG_OSR_ABR);
		langIDs.put(LANG_OSR, lid);
		langIDs_Desc.put(LANG_OSR_STR, lid);
		
		lid = new LanguageID(LANG_OTU, LANG_OTU_STR, LANG_OTU_ABR);
		langIDs.put(LANG_OTU, lid);
		langIDs_Desc.put(LANG_OTU_STR, lid);
		
		lid = new LanguageID(LANG_PRI, LANG_PRI_STR, LANG_PRI_ABR);
		langIDs.put(LANG_PRI, lid);
		langIDs_Desc.put(LANG_PRI_STR, lid);
		
		lid = new LanguageID(LANG_PRN, LANG_PRN_STR, LANG_PRN_ABR);
		langIDs.put(LANG_PRN, lid);
		langIDs_Desc.put(LANG_PRN_STR, lid);
		
		lid = new LanguageID(LANG_SAL, LANG_SAL_STR, LANG_SAL_ABR);
		langIDs.put(LANG_SAL, lid);
		langIDs_Desc.put(LANG_SAL_STR, lid);
		
		lid = new LanguageID(LANG_SET, LANG_SET_STR, LANG_SET_ABR);
		langIDs.put(LANG_SET, lid);
		langIDs_Desc.put(LANG_SET_STR, lid);
		
		lid = new LanguageID(LANG_SHI, LANG_SHI_STR, LANG_SHI_ABR);
		langIDs.put(LANG_SHI, lid);
		langIDs_Desc.put(LANG_SHI_STR, lid);
		
		lid = new LanguageID(LANG_SLA, LANG_SLA_STR, LANG_SLA_ABR);
		langIDs.put(LANG_SLA, lid);
		langIDs_Desc.put(LANG_SLA_STR, lid);
		
		lid = new LanguageID(LANG_SOH, LANG_SOH_STR, LANG_SOH_ABR);
		langIDs.put(LANG_SOH, lid);
		langIDs_Desc.put(LANG_SOH_STR, lid);
		
		lid = new LanguageID(LANG_SOM, LANG_SOM_STR, LANG_SOM_ABR);
		langIDs.put(LANG_SOM, lid);
		langIDs_Desc.put(LANG_SOM_STR, lid);
		
		lid = new LanguageID(LANG_SPI, LANG_SPI_STR, LANG_SPI_ABR);
		langIDs.put(LANG_SPI, lid);
		langIDs_Desc.put(LANG_SPI_STR, lid);
		
		lid = new LanguageID(LANG_SSA, LANG_SSA_STR, LANG_SSA_ABR);
		langIDs.put(LANG_SSA, lid);
		langIDs_Desc.put(LANG_SSA_STR, lid);
		
		lid = new LanguageID(LANG_SSI, LANG_SSI_STR, LANG_SSI_ABR);
		langIDs.put(LANG_SSI, lid);
		langIDs_Desc.put(LANG_SSI_STR, lid);
		
		lid = new LanguageID(LANG_SSL, LANG_SSL_STR, LANG_SSL_ABR);
		langIDs.put(LANG_SSL, lid);
		langIDs_Desc.put(LANG_SSL_STR, lid);
		
		lid = new LanguageID(LANG_SYU, LANG_SYU_STR, LANG_SYU_ABR);
		langIDs.put(LANG_SYU, lid);
		langIDs_Desc.put(LANG_SYU_STR, lid);
		
		lid = new LanguageID(LANG_WKA, LANG_WKA_STR, LANG_WKA_ABR);
		langIDs.put(LANG_WKA, lid);
		langIDs_Desc.put(LANG_WKA_STR, lid);
		
		lid = new LanguageID(LANG_WMA, LANG_WMA_STR, LANG_WMA_ABR);
		langIDs.put(LANG_WMA, lid);
		langIDs_Desc.put(LANG_WMA_STR, lid);
		
		lid = new LanguageID(LANG_WRR, LANG_WRR_STR, LANG_WRR_ABR);
		langIDs.put(LANG_WRR, lid);
		langIDs_Desc.put(LANG_WRR_STR, lid);
		
		// Extra extra langs
		
		lid = new LanguageID(LANG_AAR, LANG_AAR_STR, LANG_AAR_ABR);
		langIDs.put(LANG_AAR, lid);
		langIDs_Desc.put(LANG_AAR_STR, lid);
		
		lid = new LanguageID(LANG_ABA, LANG_ABA_STR, LANG_ABA_ABR);
		langIDs.put(LANG_ABA, lid);
		langIDs_Desc.put(LANG_ABA_STR, lid);
		
		lid = new LanguageID(LANG_ABE, LANG_ABE_STR, LANG_ABE_ABR);
		langIDs.put(LANG_ABE, lid);
		langIDs_Desc.put(LANG_ABE_STR, lid);
		
		lid = new LanguageID(LANG_ABK, LANG_ABK_STR, LANG_ABK_ABR);
		langIDs.put(LANG_ABK, lid);
		langIDs_Desc.put(LANG_ABK_STR, lid);
		
		lid = new LanguageID(LANG_ABN, LANG_ABN_STR, LANG_ABN_ABR);
		langIDs.put(LANG_ABN, lid);
		langIDs_Desc.put(LANG_ABN_STR, lid);
		
		lid = new LanguageID(LANG_ABU, LANG_ABU_STR, LANG_ABU_ABR);
		langIDs.put(LANG_ABU, lid);
		langIDs_Desc.put(LANG_ABU_STR, lid);
		
		lid = new LanguageID(LANG_ABZ, LANG_ABZ_STR, LANG_ABZ_ABR);
		langIDs.put(LANG_ABZ, lid);
		langIDs_Desc.put(LANG_ABZ_STR, lid);
		
		lid = new LanguageID(LANG_ACE, LANG_ACE_STR, LANG_ACE_ABR);
		langIDs.put(LANG_ACE, lid);
		langIDs_Desc.put(LANG_ACE_STR, lid);
		
		lid = new LanguageID(LANG_ACH, LANG_ACH_STR, LANG_ACH_ABR);
		langIDs.put(LANG_ACH, lid);
		langIDs_Desc.put(LANG_ACH_STR, lid);
		
		lid = new LanguageID(LANG_ACR, LANG_ACR_STR, LANG_ACR_ABR);
		langIDs.put(LANG_ACR, lid);
		langIDs_Desc.put(LANG_ACR_STR, lid);
		
		lid = new LanguageID(LANG_ACU, LANG_ACU_STR, LANG_ACU_ABR);
		langIDs.put(LANG_ACU, lid);
		langIDs_Desc.put(LANG_ACU_STR, lid);
		
		lid = new LanguageID(LANG_ADA, LANG_ADA_STR, LANG_ADA_ABR);
		langIDs.put(LANG_ADA, lid);
		langIDs_Desc.put(LANG_ADA_STR, lid);
		
		lid = new LanguageID(LANG_ADE, LANG_ADE_STR, LANG_ADE_ABR);
		langIDs.put(LANG_ADE, lid);
		langIDs_Desc.put(LANG_ADE_STR, lid);
		
		lid = new LanguageID(LANG_ADI, LANG_ADI_STR, LANG_ADI_ABR);
		langIDs.put(LANG_ADI, lid);
		langIDs_Desc.put(LANG_ADI_STR, lid);
		
		lid = new LanguageID(LANG_ADN, LANG_ADN_STR, LANG_ADN_ABR);
		langIDs.put(LANG_ADN, lid);
		langIDs_Desc.put(LANG_ADN_STR, lid);
		
		lid = new LanguageID(LANG_ADY, LANG_ADY_STR, LANG_ADY_ABR);
		langIDs.put(LANG_ADY, lid);
		langIDs_Desc.put(LANG_ADY_STR, lid);
		
		lid = new LanguageID(LANG_ADZ, LANG_ADZ_STR, LANG_ADZ_ABR);
		langIDs.put(LANG_ADZ, lid);
		langIDs_Desc.put(LANG_ADZ_STR, lid);
		
		lid = new LanguageID(LANG_AG, LANG_AG_STR, LANG_AG_ABR);
		langIDs.put(LANG_AG, lid);
		langIDs_Desc.put(LANG_AG_STR, lid);
		
		lid = new LanguageID(LANG_AGH, LANG_AGH_STR, LANG_AGH_ABR);
		langIDs.put(LANG_AGH, lid);
		langIDs_Desc.put(LANG_AGH_STR, lid);
		
		lid = new LanguageID(LANG_AGU, LANG_AGU_STR, LANG_AGU_ABR);
		langIDs.put(LANG_AGU, lid);
		langIDs_Desc.put(LANG_AGU_STR, lid);
		
		lid = new LanguageID(LANG_AIN, LANG_AIN_STR, LANG_AIN_ABR);
		langIDs.put(LANG_AIN, lid);
		langIDs_Desc.put(LANG_AIN_STR, lid);
		
		lid = new LanguageID(LANG_AKA, LANG_AKA_STR, LANG_AKA_ABR);
		langIDs.put(LANG_AKA, lid);
		langIDs_Desc.put(LANG_AKA_STR, lid);
		
		lid = new LanguageID(LANG_AKK, LANG_AKK_STR, LANG_AKK_ABR);
		langIDs.put(LANG_AKK, lid);
		langIDs_Desc.put(LANG_AKK_STR, lid);
		
		lid = new LanguageID(LANG_AKL, LANG_AKL_STR, LANG_AKL_ABR);
		langIDs.put(LANG_AKL, lid);
		langIDs_Desc.put(LANG_AKL_STR, lid);
		
		lid = new LanguageID(LANG_AKS, LANG_AKS_STR, LANG_AKS_ABR);
		langIDs.put(LANG_AKS, lid);
		langIDs_Desc.put(LANG_AKS_STR, lid);
		
		lid = new LanguageID(LANG_AL, LANG_AL_STR, LANG_AL_ABR);
		langIDs.put(LANG_AL, lid);
		langIDs_Desc.put(LANG_AL_STR, lid);
		
		lid = new LanguageID(LANG_ALC, LANG_ALC_STR, LANG_ALC_ABR);
		langIDs.put(LANG_ALC, lid);
		langIDs_Desc.put(LANG_ALC_STR, lid);
		
		lid = new LanguageID(LANG_ALE, LANG_ALE_STR, LANG_ALE_ABR);
		langIDs.put(LANG_ALE, lid);
		langIDs_Desc.put(LANG_ALE_STR, lid);
		
		lid = new LanguageID(LANG_ALM, LANG_ALM_STR, LANG_ALM_ABR);
		langIDs.put(LANG_ALM, lid);
		langIDs_Desc.put(LANG_ALM_STR, lid);
		
		lid = new LanguageID(LANG_ALT, LANG_ALT_STR, LANG_ALT_ABR);
		langIDs.put(LANG_ALT, lid);
		langIDs_Desc.put(LANG_ALT_STR, lid);
		
		lid = new LanguageID(LANG_ALU, LANG_ALU_STR, LANG_ALU_ABR);
		langIDs.put(LANG_ALU, lid);
		langIDs_Desc.put(LANG_ALU_STR, lid);
		
		lid = new LanguageID(LANG_ALV, LANG_ALV_STR, LANG_ALV_ABR);
		langIDs.put(LANG_ALV, lid);
		langIDs_Desc.put(LANG_ALV_STR, lid);
		
		lid = new LanguageID(LANG_AMA, LANG_AMA_STR, LANG_AMA_ABR);
		langIDs.put(LANG_AMA, lid);
		langIDs_Desc.put(LANG_AMA_STR, lid);
		
		lid = new LanguageID(LANG_AMB, LANG_AMB_STR, LANG_AMB_ABR);
		langIDs.put(LANG_AMB, lid);
		langIDs_Desc.put(LANG_AMB_STR, lid);
		
		lid = new LanguageID(LANG_AMI, LANG_AMI_STR, LANG_AMI_ABR);
		langIDs.put(LANG_AMI, lid);
		langIDs_Desc.put(LANG_AMI_STR, lid);
		
		lid = new LanguageID(LANG_AMM, LANG_AMM_STR, LANG_AMM_ABR);
		langIDs.put(LANG_AMM, lid);
		langIDs_Desc.put(LANG_AMM_STR, lid);
		
		lid = new LanguageID(LANG_AMN, LANG_AMN_STR, LANG_AMN_ABR);
		langIDs.put(LANG_AMN, lid);
		langIDs_Desc.put(LANG_AMN_STR, lid);
		
		lid = new LanguageID(LANG_AMO, LANG_AMO_STR, LANG_AMO_ABR);
		langIDs.put(LANG_AMO, lid);
		langIDs_Desc.put(LANG_AMO_STR, lid);
		
		lid = new LanguageID(LANG_AMS, LANG_AMS_STR, LANG_AMS_ABR);
		langIDs.put(LANG_AMS, lid);
		langIDs_Desc.put(LANG_AMS_STR, lid);
		
		lid = new LanguageID(LANG_AMU, LANG_AMU_STR, LANG_AMU_ABR);
		langIDs.put(LANG_AMU, lid);
		langIDs_Desc.put(LANG_AMU_STR, lid);
		
		lid = new LanguageID(LANG_AMZ, LANG_AMZ_STR, LANG_AMZ_ABR);
		langIDs.put(LANG_AMZ, lid);
		langIDs_Desc.put(LANG_AMZ_STR, lid);
		
		lid = new LanguageID(LANG_ANB, LANG_ANB_STR, LANG_ANB_ABR);
		langIDs.put(LANG_ANB, lid);
		langIDs_Desc.put(LANG_ANB_STR, lid);
		
		lid = new LanguageID(LANG_ANC, LANG_ANC_STR, LANG_ANC_ABR);
		langIDs.put(LANG_ANC, lid);
		langIDs_Desc.put(LANG_ANC_STR, lid);
		
		lid = new LanguageID(LANG_ANGA, LANG_ANGA_STR, LANG_ANGA_ABR);
		langIDs.put(LANG_ANGA, lid);
		langIDs_Desc.put(LANG_ANGA_STR, lid);
		
		lid = new LanguageID(LANG_ANGL, LANG_ANGL_STR, LANG_ANGL_ABR);
		langIDs.put(LANG_ANGL, lid);
		langIDs_Desc.put(LANG_ANGL_STR, lid);
		
		lid = new LanguageID(LANG_ANGR, LANG_ANGR_STR, LANG_ANGR_ABR);
		langIDs.put(LANG_ANGR, lid);
		langIDs_Desc.put(LANG_ANGR_STR, lid);
		
		lid = new LanguageID(LANG_ANK, LANG_ANK_STR, LANG_ANK_ABR);
		langIDs.put(LANG_ANK, lid);
		langIDs_Desc.put(LANG_ANK_STR, lid);
		
		lid = new LanguageID(LANG_ANR, LANG_ANR_STR, LANG_ANR_ABR);
		langIDs.put(LANG_ANR, lid);
		langIDs_Desc.put(LANG_ANR_STR, lid);
		
		lid = new LanguageID(LANG_ANS, LANG_ANS_STR, LANG_ANS_ABR);
		langIDs.put(LANG_ANS, lid);
		langIDs_Desc.put(LANG_ANS_STR, lid);
		
		lid = new LanguageID(LANG_ANU, LANG_ANU_STR, LANG_ANU_ABR);
		langIDs.put(LANG_ANU, lid);
		langIDs_Desc.put(LANG_ANU_STR, lid);
		
		lid = new LanguageID(LANG_ANW, LANG_ANW_STR, LANG_ANW_ABR);
		langIDs.put(LANG_ANW, lid);
		langIDs_Desc.put(LANG_ANW_STR, lid);
		
		lid = new LanguageID(LANG_APA, LANG_APA_STR, LANG_APA_ABR);
		langIDs.put(LANG_APA, lid);
		langIDs_Desc.put(LANG_APA_STR, lid);
		
		lid = new LanguageID(LANG_ARAM, LANG_ARAM_STR, LANG_ARAM_ABR);
		langIDs.put(LANG_ARAM, lid);
		langIDs_Desc.put(LANG_ARAM_STR, lid);
		
		lid = new LanguageID(LANG_ARAP, LANG_ARAP_STR, LANG_ARAP_ABR);
		langIDs.put(LANG_ARAP, lid);
		langIDs_Desc.put(LANG_ARAP_STR, lid);
		
		lid = new LanguageID(LANG_ARAW, LANG_ARAW_STR, LANG_ARAW_ABR);
		langIDs.put(LANG_ARAW, lid);
		langIDs_Desc.put(LANG_ARAW_STR, lid);
		
		lid = new LanguageID(LANG_ARB, LANG_ARB_STR, LANG_ARB_ABR);
		langIDs.put(LANG_ARB, lid);
		langIDs_Desc.put(LANG_ARB_STR, lid);
		
		lid = new LanguageID(LANG_ARC, LANG_ARC_STR, LANG_ARC_ABR);
		langIDs.put(LANG_ARC, lid);
		langIDs_Desc.put(LANG_ARC_STR, lid);
		
		lid = new LanguageID(LANG_ARE, LANG_ARE_STR, LANG_ARE_ABR);
		langIDs.put(LANG_ARE, lid);
		langIDs_Desc.put(LANG_ARE_STR, lid);
		
		lid = new LanguageID(LANG_ARG, LANG_ARG_STR, LANG_ARG_ABR);
		langIDs.put(LANG_ARG, lid);
		langIDs_Desc.put(LANG_ARG_STR, lid);
		
		lid = new LanguageID(LANG_ARI, LANG_ARI_STR, LANG_ARI_ABR);
		langIDs.put(LANG_ARI, lid);
		langIDs_Desc.put(LANG_ARI_STR, lid);
		
		lid = new LanguageID(LANG_ASS, LANG_ASS_STR, LANG_ASS_ABR);
		langIDs.put(LANG_ASS, lid);
		langIDs_Desc.put(LANG_ASS_STR, lid);
		
		lid = new LanguageID(LANG_ATA, LANG_ATA_STR, LANG_ATA_ABR);
		langIDs.put(LANG_ATA, lid);
		langIDs_Desc.put(LANG_ATA_STR, lid);
		
		lid = new LanguageID(LANG_AUH, LANG_AUH_STR, LANG_AUH_ABR);
		langIDs.put(LANG_AUH, lid);
		langIDs_Desc.put(LANG_AUH_STR, lid);
		
		lid = new LanguageID(LANG_AUHE, LANG_AUHE_STR, LANG_AUHE_ABR);
		langIDs.put(LANG_AUHE, lid);
		langIDs_Desc.put(LANG_AUHE_STR, lid);
		
		lid = new LanguageID(LANG_AVE, LANG_AVE_STR, LANG_AVE_ABR);
		langIDs.put(LANG_AVE, lid);
		langIDs_Desc.put(LANG_AVE_STR, lid);
		
		lid = new LanguageID(LANG_AWAB, LANG_AWAB_STR, LANG_AWAB_ABR);
		langIDs.put(LANG_AWAB, lid);
		langIDs_Desc.put(LANG_AWAB_STR, lid);
		
		lid = new LanguageID(LANG_AWAD, LANG_AWAD_STR, LANG_AWAD_ABR);
		langIDs.put(LANG_AWAD, lid);
		langIDs_Desc.put(LANG_AWAD_STR, lid);
		
		lid = new LanguageID(LANG_AYM, LANG_AYM_STR, LANG_AYM_ABR);
		langIDs.put(LANG_AYM, lid);
		langIDs_Desc.put(LANG_AYM_STR, lid);
		
		lid = new LanguageID(LANG_AYN, LANG_AYN_STR, LANG_AYN_ABR);
		langIDs.put(LANG_AYN, lid);
		langIDs_Desc.put(LANG_AYN_STR, lid);
		
		lid = new LanguageID(LANG_AZE, LANG_AZE_STR, LANG_AZE_ABR);
		langIDs.put(LANG_AZE, lid);
		langIDs_Desc.put(LANG_AZE_STR, lid);
		
		lid = new LanguageID(LANG_BAC, LANG_BAC_STR, LANG_BAC_ABR);
		langIDs.put(LANG_BAC, lid);
		langIDs_Desc.put(LANG_BAC_STR, lid);
		
		lid = new LanguageID(LANG_BAE, LANG_BAE_STR, LANG_BAE_ABR);
		langIDs.put(LANG_BAE, lid);
		langIDs_Desc.put(LANG_BAE_STR, lid);
		
		lid = new LanguageID(LANG_BALI, LANG_BALI_STR, LANG_BALI_ABR);
		langIDs.put(LANG_BALI, lid);
		langIDs_Desc.put(LANG_BALI_STR, lid);
		
		lid = new LanguageID(LANG_BALT, LANG_BALT_STR, LANG_BALT_ABR);
		langIDs.put(LANG_BALT, lid);
		langIDs_Desc.put(LANG_BALT_STR, lid);
		
		lid = new LanguageID(LANG_BALU, LANG_BALU_STR, LANG_BALU_ABR);
		langIDs.put(LANG_BALU, lid);
		langIDs_Desc.put(LANG_BALU_STR, lid);
		
		lid = new LanguageID(LANG_BAND, LANG_BAND_STR, LANG_BAND_ABR);
		langIDs.put(LANG_BAND, lid);
		langIDs_Desc.put(LANG_BAND_STR, lid);
		
		lid = new LanguageID(LANG_BANJ, LANG_BANJ_STR, LANG_BANJ_ABR);
		langIDs.put(LANG_BANJ, lid);
		langIDs_Desc.put(LANG_BANJ_STR, lid);
		
		lid = new LanguageID(LANG_BANY, LANG_BANY_STR, LANG_BANY_ABR);
		langIDs.put(LANG_BANY, lid);
		langIDs_Desc.put(LANG_BANY_STR, lid);
		
		lid = new LanguageID(LANG_BAR, LANG_BAR_STR, LANG_BAR_ABR);
		langIDs.put(LANG_BAR, lid);
		langIDs_Desc.put(LANG_BAR_STR, lid);
		
		lid = new LanguageID(LANG_BAS, LANG_BAS_STR, LANG_BAS_ABR);
		langIDs.put(LANG_BAS, lid);
		langIDs_Desc.put(LANG_BAS_STR, lid);
		
		lid = new LanguageID(LANG_BAT, LANG_BAT_STR, LANG_BAT_ABR);
		langIDs.put(LANG_BAT, lid);
		langIDs_Desc.put(LANG_BAT_STR, lid);
		
		lid = new LanguageID(LANG_BAU, LANG_BAU_STR, LANG_BAU_ABR);
		langIDs.put(LANG_BAU, lid);
		langIDs_Desc.put(LANG_BAU_STR, lid);
		
		lid = new LanguageID(LANG_BAV, LANG_BAV_STR, LANG_BAV_ABR);
		langIDs.put(LANG_BAV, lid);
		langIDs_Desc.put(LANG_BAV_STR, lid);
		
		lid = new LanguageID(LANG_BCD, LANG_BCD_STR, LANG_BCD_ABR);
		langIDs.put(LANG_BCD, lid);
		langIDs_Desc.put(LANG_BCD_STR, lid);
		
		lid = new LanguageID(LANG_BEJ, LANG_BEJ_STR, LANG_BEJ_ABR);
		langIDs.put(LANG_BEJ, lid);
		langIDs_Desc.put(LANG_BEJ_STR, lid);
		
		lid = new LanguageID(LANG_BET, LANG_BET_STR, LANG_BET_ABR);
		langIDs.put(LANG_BET, lid);
		langIDs_Desc.put(LANG_BET_STR, lid);
		
		lid = new LanguageID(LANG_BHO, LANG_BHO_STR, LANG_BHO_ABR);
		langIDs.put(LANG_BHO, lid);
		langIDs_Desc.put(LANG_BHO_STR, lid);
		
		lid = new LanguageID(LANG_BIA, LANG_BIA_STR, LANG_BIA_ABR);
		langIDs.put(LANG_BIA, lid);
		langIDs_Desc.put(LANG_BIA_STR, lid);
		
		lid = new LanguageID(LANG_BIC, LANG_BIC_STR, LANG_BIC_ABR);
		langIDs.put(LANG_BIC, lid);
		langIDs_Desc.put(LANG_BIC_STR, lid);
		
		lid = new LanguageID(LANG_BIK, LANG_BIK_STR, LANG_BIK_ABR);
		langIDs.put(LANG_BIK, lid);
		langIDs_Desc.put(LANG_BIK_STR, lid);
		
		lid = new LanguageID(LANG_BIN, LANG_BIN_STR, LANG_BIN_ABR);
		langIDs.put(LANG_BIN, lid);
		langIDs_Desc.put(LANG_BIN_STR, lid);
		
		lid = new LanguageID(LANG_BIS, LANG_BIS_STR, LANG_BIS_ABR);
		langIDs.put(LANG_BIS, lid);
		langIDs_Desc.put(LANG_BIS_STR, lid);
		
		lid = new LanguageID(LANG_BL, LANG_BL_STR, LANG_BL_ABR);
		langIDs.put(LANG_BL, lid);
		langIDs_Desc.put(LANG_BL_STR, lid);
		
		lid = new LanguageID(LANG_BON, LANG_BON_STR, LANG_BON_ABR);
		langIDs.put(LANG_BON, lid);
		langIDs_Desc.put(LANG_BON_STR, lid);
		
		lid = new LanguageID(LANG_BOR, LANG_BOR_STR, LANG_BOR_ABR);
		langIDs.put(LANG_BOR, lid);
		langIDs_Desc.put(LANG_BOR_STR, lid);
		
		lid = new LanguageID(LANG_BPLP, LANG_BPLP_STR, LANG_BPLP_ABR);
		langIDs.put(LANG_BPLP, lid);
		langIDs_Desc.put(LANG_BPLP_STR, lid);
		
		lid = new LanguageID(LANG_BRA, LANG_BRA_STR, LANG_BRA_ABR);
		langIDs.put(LANG_BRA, lid);
		langIDs_Desc.put(LANG_BRA_STR, lid);
		
		lid = new LanguageID(LANG_BRI, LANG_BRI_STR, LANG_BRI_ABR);
		langIDs.put(LANG_BRI, lid);
		langIDs_Desc.put(LANG_BRI_STR, lid);
		
		lid = new LanguageID(LANG_BUB, LANG_BUB_STR, LANG_BUB_ABR);
		langIDs.put(LANG_BUB, lid);
		langIDs_Desc.put(LANG_BUB_STR, lid);
		
		lid = new LanguageID(LANG_BUG, LANG_BUG_STR, LANG_BUG_ABR);
		langIDs.put(LANG_BUG, lid);
		langIDs_Desc.put(LANG_BUG_STR, lid);
		
		lid = new LanguageID(LANG_BUH, LANG_BUH_STR, LANG_BUH_ABR);
		langIDs.put(LANG_BUH, lid);
		langIDs_Desc.put(LANG_BUH_STR, lid);
		
		lid = new LanguageID(LANG_BUL, LANG_BUL_STR, LANG_BUL_ABR);
		langIDs.put(LANG_BUL, lid);
		langIDs_Desc.put(LANG_BUL_STR, lid);
		
		lid = new LanguageID(LANG_BUN, LANG_BUN_STR, LANG_BUN_ABR);
		langIDs.put(LANG_BUN, lid);
		langIDs_Desc.put(LANG_BUN_STR, lid);
		
		lid = new LanguageID(LANG_BUR, LANG_BUR_STR, LANG_BUR_ABR);
		langIDs.put(LANG_BUR, lid);
		langIDs_Desc.put(LANG_BUR_STR, lid);
		
		lid = new LanguageID(LANG_BUY, LANG_BUY_STR, LANG_BUY_ABR);
		langIDs.put(LANG_BUY, lid);
		langIDs_Desc.put(LANG_BUY_STR, lid);
		
		lid = new LanguageID(LANG_CAL, LANG_CAL_STR, LANG_CAL_ABR);
		langIDs.put(LANG_CAL, lid);
		langIDs_Desc.put(LANG_CAL_STR, lid);
		
		lid = new LanguageID(LANG_CAN, LANG_CAN_STR, LANG_CAN_ABR);
		langIDs.put(LANG_CAN, lid);
		langIDs_Desc.put(LANG_CAN_STR, lid);
		
		lid = new LanguageID(LANG_CAR, LANG_CAR_STR, LANG_CAR_ABR);
		langIDs.put(LANG_CAR, lid);
		langIDs_Desc.put(LANG_CAR_STR, lid);
		
		lid = new LanguageID(LANG_CAS, LANG_CAS_STR, LANG_CAS_ABR);
		langIDs.put(LANG_CAS, lid);
		langIDs_Desc.put(LANG_CAS_STR, lid);
		
		lid = new LanguageID(LANG_CAT, LANG_CAT_STR, LANG_CAT_ABR);
		langIDs.put(LANG_CAT, lid);
		langIDs_Desc.put(LANG_CAT_STR, lid);
		
		lid = new LanguageID(LANG_CEB, LANG_CEB_STR, LANG_CEB_ABR);
		langIDs.put(LANG_CEB, lid);
		langIDs_Desc.put(LANG_CEB_STR, lid);
		
		lid = new LanguageID(LANG_CGR, LANG_CGR_STR, LANG_CGR_ABR);
		langIDs.put(LANG_CGR, lid);
		langIDs_Desc.put(LANG_CGR_STR, lid);
		
		lid = new LanguageID(LANG_CHAG, LANG_CHAG_STR, LANG_CHAG_ABR);
		langIDs.put(LANG_CHAG, lid);
		langIDs_Desc.put(LANG_CHAG_STR, lid);
		
		lid = new LanguageID(LANG_CHC, LANG_CHC_STR, LANG_CHC_ABR);
		langIDs.put(LANG_CHC, lid);
		langIDs_Desc.put(LANG_CHC_STR, lid);
		
		lid = new LanguageID(LANG_CHEC, LANG_CHEC_STR, LANG_CHEC_ABR);
		langIDs.put(LANG_CHEC, lid);
		langIDs_Desc.put(LANG_CHEC_STR, lid);
		
		lid = new LanguageID(LANG_CHEY, LANG_CHEY_STR, LANG_CHEY_ABR);
		langIDs.put(LANG_CHEY, lid);
		langIDs_Desc.put(LANG_CHEY_STR, lid);
		
		lid = new LanguageID(LANG_CHIC, LANG_CHIC_STR, LANG_CHIC_ABR);
		langIDs.put(LANG_CHIC, lid);
		langIDs_Desc.put(LANG_CHIC_STR, lid);
		
		lid = new LanguageID(LANG_CHIN, LANG_CHIN_STR, LANG_CHIN_ABR);
		langIDs.put(LANG_CHIN, lid);
		langIDs_Desc.put(LANG_CHIN_STR, lid);
		
		lid = new LanguageID(LANG_CHIQ, LANG_CHIQ_STR, LANG_CHIQ_ABR);
		langIDs.put(LANG_CHIQ, lid);
		langIDs_Desc.put(LANG_CHIQ_STR, lid);
				
		lid = new LanguageID(LANG_CHIR, LANG_CHIR_STR, LANG_CHIR_ABR);
		langIDs.put(LANG_CHIR, lid);
		langIDs_Desc.put(LANG_CHIR_STR, lid);
				
		lid = new LanguageID(LANG_CHJ, LANG_CHJ_STR, LANG_CHJ_ABR);
		langIDs.put(LANG_CHJ, lid);
		langIDs_Desc.put(LANG_CHJ_STR, lid);
		
		lid = new LanguageID(LANG_CHMI, LANG_CHMI_STR, LANG_CHMI_ABR);
		langIDs.put(LANG_CHMI, lid);
		langIDs_Desc.put(LANG_CHMI_STR, lid);
		
		lid = new LanguageID(LANG_CHOC, LANG_CHOC_STR, LANG_CHOC_ABR);
		langIDs.put(LANG_CHOC, lid);
		langIDs_Desc.put(LANG_CHOC_STR, lid);
		
		lid = new LanguageID(LANG_CHOL, LANG_CHOL_STR, LANG_CHOL_ABR);
		langIDs.put(LANG_CHOL, lid);
		langIDs_Desc.put(LANG_CHOL_STR, lid);
		
		lid = new LanguageID(LANG_CHOR, LANG_CHOR_STR, LANG_CHOR_ABR);
		langIDs.put(LANG_CHOR, lid);
		langIDs_Desc.put(LANG_CHOR_STR, lid);
		
		lid = new LanguageID(LANG_CHPE, LANG_CHPE_STR, LANG_CHPE_ABR);
		langIDs.put(LANG_CHPE, lid);
		langIDs_Desc.put(LANG_CHPE_STR, lid);
		
		lid = new LanguageID(LANG_CHRU, LANG_CHRU_STR, LANG_CHRU_ABR);
		langIDs.put(LANG_CHRU, lid);
		langIDs_Desc.put(LANG_CHRU_STR, lid);
		
		lid = new LanguageID(LANG_CHUK, LANG_CHUK_STR, LANG_CHUK_ABR);
		langIDs.put(LANG_CHUK, lid);
		langIDs_Desc.put(LANG_CHUK_STR, lid);
		
		lid = new LanguageID(LANG_CHUV, LANG_CHUV_STR, LANG_CHUV_ABR);
		langIDs.put(LANG_CHUV, lid);
		langIDs_Desc.put(LANG_CHUV_STR, lid);
		
		lid = new LanguageID(LANG_CIA, LANG_CIA_STR, LANG_CIA_ABR);
		langIDs.put(LANG_CIA, lid);
		langIDs_Desc.put(LANG_CIA_STR, lid);
		
		lid = new LanguageID(LANG_CIM, LANG_CIM_STR, LANG_CIM_ABR);
		langIDs.put(LANG_CIM, lid);
		langIDs_Desc.put(LANG_CIM_STR, lid);
		
		lid = new LanguageID(LANG_CKA, LANG_CKA_STR, LANG_CKA_ABR);
		langIDs.put(LANG_CKA, lid);
		langIDs_Desc.put(LANG_CKA_STR, lid);
		
		lid = new LanguageID(LANG_CNA, LANG_CNA_STR, LANG_CNA_ABR);
		langIDs.put(LANG_CNA, lid);
		langIDs_Desc.put(LANG_CNA_STR, lid);
		
		lid = new LanguageID(LANG_COMA, LANG_COMA_STR, LANG_COMA_ABR);
		langIDs.put(LANG_COMA, lid);
		langIDs_Desc.put(LANG_COMA_STR, lid);
		
		lid = new LanguageID(LANG_COMI, LANG_COMI_STR, LANG_COMI_ABR);
		langIDs.put(LANG_COMI, lid);
		langIDs_Desc.put(LANG_COMI_STR, lid);
		
		lid = new LanguageID(LANG_COMO, LANG_COMO_STR, LANG_COMO_ABR);
		langIDs.put(LANG_COMO, lid);
		langIDs_Desc.put(LANG_COMO_STR, lid);
		
		lid = new LanguageID(LANG_COMX, LANG_COMX_STR, LANG_COMX_ABR);
		langIDs.put(LANG_COMX, lid);
		langIDs_Desc.put(LANG_COMX_STR, lid);
		
		lid = new LanguageID(LANG_COPT, LANG_COPT_STR, LANG_COPT_ABR);
		langIDs.put(LANG_COPT, lid);
		langIDs_Desc.put(LANG_COPT_STR, lid);
		
		lid = new LanguageID(LANG_COWL, LANG_COWL_STR, LANG_COWL_ABR);
		langIDs.put(LANG_COWL, lid);
		langIDs_Desc.put(LANG_COWL_STR, lid);
		
		lid = new LanguageID(LANG_CRE, LANG_CRE_STR, LANG_CRE_ABR);
		langIDs.put(LANG_CRE, lid);
		langIDs_Desc.put(LANG_CRE_STR, lid);
		
		lid = new LanguageID(LANG_CRT, LANG_CRT_STR, LANG_CRT_ABR);
		langIDs.put(LANG_CRT, lid);
		langIDs_Desc.put(LANG_CRT_STR, lid);
		
		lid = new LanguageID(LANG_CT, LANG_CT_STR, LANG_CT_ABR);
		langIDs.put(LANG_CT, lid);
		langIDs_Desc.put(LANG_CT_STR, lid);
		
		lid = new LanguageID(LANG_CTA, LANG_CTA_STR, LANG_CTA_ABR);
		langIDs.put(LANG_CTA, lid);
		langIDs_Desc.put(LANG_CTA_STR, lid);
		
		lid = new LanguageID(LANG_CTAT, LANG_CTAT_STR, LANG_CTAT_ABR);
		langIDs.put(LANG_CTAT, lid);
		langIDs_Desc.put(LANG_CTAT_STR, lid);
		
		lid = new LanguageID(LANG_CTDU, LANG_CTDU_STR, LANG_CTDU_ABR);
		langIDs.put(LANG_CTDU, lid);
		langIDs_Desc.put(LANG_CTDU_STR, lid);
		
		lid = new LanguageID(LANG_CTSY, LANG_CTSY_STR, LANG_CTSY_ABR);
		langIDs.put(LANG_CTSY, lid);
		langIDs_Desc.put(LANG_CTSY_STR, lid);
		
		lid = new LanguageID(LANG_CTTA, LANG_CTTA_STR, LANG_CTTA_ABR);
		langIDs.put(LANG_CTTA, lid);
		langIDs_Desc.put(LANG_CTTA_STR, lid);
		
		lid = new LanguageID(LANG_CUI, LANG_CUI_STR, LANG_CUI_ABR);
		langIDs.put(LANG_CUI, lid);
		langIDs_Desc.put(LANG_CUI_STR, lid);
		
		lid = new LanguageID(LANG_DAC, LANG_DAC_STR, LANG_DAC_ABR);
		langIDs.put(LANG_DAC, lid);
		langIDs_Desc.put(LANG_DAC_STR, lid);
		
		lid = new LanguageID(LANG_DAD, LANG_DAD_STR, LANG_DAD_ABR);
		langIDs.put(LANG_DAD, lid);
		langIDs_Desc.put(LANG_DAD_STR, lid);
		
		lid = new LanguageID(LANG_DAK, LANG_DAK_STR, LANG_DAK_ABR);
		langIDs.put(LANG_DAK, lid);
		langIDs_Desc.put(LANG_DAK_STR, lid);
		
		lid = new LanguageID(LANG_DAL, LANG_DAL_STR, LANG_DAL_ABR);
		langIDs.put(LANG_DAL, lid);
		langIDs_Desc.put(LANG_DAL_STR, lid);
		
		lid = new LanguageID(LANG_DARG, LANG_DARG_STR, LANG_DARG_ABR);
		langIDs.put(LANG_DARG, lid);
		langIDs_Desc.put(LANG_DARG_STR, lid);
		
		lid = new LanguageID(LANG_DARL, LANG_DARL_STR, LANG_DARL_ABR);
		langIDs.put(LANG_DARL, lid);
		langIDs_Desc.put(LANG_DARL_STR, lid);
		
		lid = new LanguageID(LANG_DHI, LANG_DHI_STR, LANG_DHI_ABR);
		langIDs.put(LANG_DHI, lid);
		langIDs_Desc.put(LANG_DHI_STR, lid);
		
		lid = new LanguageID(LANG_DHU, LANG_DHU_STR, LANG_DHU_ABR);
		langIDs.put(LANG_DHU, lid);
		langIDs_Desc.put(LANG_DHU_STR, lid);
		
		lid = new LanguageID(LANG_DIE, LANG_DIE_STR, LANG_DIE_ABR);
		langIDs.put(LANG_DIE, lid);
		langIDs_Desc.put(LANG_DIE_STR, lid);
		
		lid = new LanguageID(LANG_DII, LANG_DII_STR, LANG_DII_ABR);
		langIDs.put(LANG_DII, lid);
		langIDs_Desc.put(LANG_DII_STR, lid);
		
		lid = new LanguageID(LANG_DIN, LANG_DIN_STR, LANG_DIN_ABR);
		langIDs.put(LANG_DIN, lid);
		langIDs_Desc.put(LANG_DIN_STR, lid);
		
		lid = new LanguageID(LANG_DKI, LANG_DKI_STR, LANG_DKI_ABR);
		langIDs.put(LANG_DKI, lid);
		langIDs_Desc.put(LANG_DKI_STR, lid);
		
		lid = new LanguageID(LANG_DNI, LANG_DNI_STR, LANG_DNI_ABR);
		langIDs.put(LANG_DNI, lid);
		langIDs_Desc.put(LANG_DNI_STR, lid);
		
		lid = new LanguageID(LANG_DOG, LANG_DOG_STR, LANG_DOG_ABR);
		langIDs.put(LANG_DOG, lid);
		langIDs_Desc.put(LANG_DOG_STR, lid);
		
		lid = new LanguageID(LANG_DOL, LANG_DOL_STR, LANG_DOL_ABR);
		langIDs.put(LANG_DOL, lid);
		langIDs_Desc.put(LANG_DOL_STR, lid);
		
		lid = new LanguageID(LANG_DOR, LANG_DOR_STR, LANG_DOR_ABR);
		langIDs.put(LANG_DOR, lid);
		langIDs_Desc.put(LANG_DOR_STR, lid);
		
		lid = new LanguageID(LANG_DRE, LANG_DRE_STR, LANG_DRE_ABR);
		langIDs.put(LANG_DRE, lid);
		langIDs_Desc.put(LANG_DRE_STR, lid);
		
		lid = new LanguageID(LANG_DUN, LANG_DUN_STR, LANG_DUN_ABR);
		langIDs.put(LANG_DUN, lid);
		langIDs_Desc.put(LANG_DUN_STR, lid);
		
		lid = new LanguageID(LANG_DYI, LANG_DYI_STR, LANG_DYI_ABR);
		langIDs.put(LANG_DYI, lid);
		langIDs_Desc.put(LANG_DYI_STR, lid);
		
		lid = new LanguageID(LANG_EAR, LANG_EAR_STR, LANG_EAR_ABR);
		langIDs.put(LANG_EAR, lid);
		langIDs_Desc.put(LANG_EAR_STR, lid);
		
		lid = new LanguageID(LANG_EBL, LANG_EBL_STR, LANG_EBL_ABR);
		langIDs.put(LANG_EBL, lid);
		langIDs_Desc.put(LANG_EBL_STR, lid);
		
		lid = new LanguageID(LANG_ECH, LANG_ECH_STR, LANG_ECH_ABR);
		langIDs.put(LANG_ECH, lid);
		langIDs_Desc.put(LANG_ECH_STR, lid);
		
		lid = new LanguageID(LANG_ECI, LANG_ECI_STR, LANG_ECI_ABR);
		langIDs.put(LANG_ECI, lid);
		langIDs_Desc.put(LANG_ECI_STR, lid);
		
		lid = new LanguageID(LANG_ECR, LANG_ECR_STR, LANG_ECR_ABR);
		langIDs.put(LANG_ECR, lid);
		langIDs_Desc.put(LANG_ECR_STR, lid);
		
		lid = new LanguageID(LANG_EDO, LANG_EDO_STR, LANG_EDO_ABR);
		langIDs.put(LANG_EDO, lid);
		langIDs_Desc.put(LANG_EDO_STR, lid);
		
		lid = new LanguageID(LANG_EDOM, LANG_EDOM_STR, LANG_EDOM_ABR);
		langIDs.put(LANG_EDOM, lid);
		langIDs_Desc.put(LANG_EDOM_STR, lid);
		
		lid = new LanguageID(LANG_EFU, LANG_EFU_STR, LANG_EFU_ABR);
		langIDs.put(LANG_EFU, lid);
		langIDs_Desc.put(LANG_EFU_STR, lid);
		
		lid = new LanguageID(LANG_EG, LANG_EG_STR, LANG_EG_ABR);
		langIDs.put(LANG_EG, lid);
		langIDs_Desc.put(LANG_EG_STR, lid);
		
		lid = new LanguageID(LANG_EGA, LANG_EGA_STR, LANG_EGA_ABR);
		langIDs.put(LANG_EGA, lid);
		langIDs_Desc.put(LANG_EGA_STR, lid);
		
		lid = new LanguageID(LANG_ELFD, LANG_ELFD_STR, LANG_ELFD_ABR);
		langIDs.put(LANG_ELFD, lid);
		langIDs_Desc.put(LANG_ELFD_STR, lid);
		
		lid = new LanguageID(LANG_ERZ, LANG_ERZ_STR, LANG_ERZ_ABR);
		langIDs.put(LANG_ERZ, lid);
		langIDs_Desc.put(LANG_ERZ_STR, lid);
		
		lid = new LanguageID(LANG_ESA, LANG_ESA_STR, LANG_ESA_ABR);
		langIDs.put(LANG_ESA, lid);
		langIDs_Desc.put(LANG_ESA_STR, lid);
		
		lid = new LanguageID(LANG_ETR, LANG_ETR_STR, LANG_ETR_ABR);
		langIDs.put(LANG_ETR, lid);
		langIDs_Desc.put(LANG_ETR_STR, lid);
		
		lid = new LanguageID(LANG_EVE, LANG_EVE_STR, LANG_EVE_ABR);
		langIDs.put(LANG_EVE, lid);
		langIDs_Desc.put(LANG_EVE_STR, lid);
		
		lid = new LanguageID(LANG_EWE, LANG_EWE_STR, LANG_EWE_ABR);
		langIDs.put(LANG_EWE, lid);
		langIDs_Desc.put(LANG_EWE_STR, lid);
		
		lid = new LanguageID(LANG_EXT, LANG_EXT_STR, LANG_EXT_ABR);
		langIDs.put(LANG_EXT, lid);
		langIDs_Desc.put(LANG_EXT_STR, lid);
		
		lid = new LanguageID(LANG_FAL, LANG_FAL_STR, LANG_FAL_ABR);
		langIDs.put(LANG_FAL, lid);
		langIDs_Desc.put(LANG_FAL_STR, lid);
		
		lid = new LanguageID(LANG_FH, LANG_FH_STR, LANG_FH_ABR);
		langIDs.put(LANG_FH, lid);
		langIDs_Desc.put(LANG_FH_STR, lid);
		
		lid = new LanguageID(LANG_FLE, LANG_FLE_STR, LANG_FLE_ABR);
		langIDs.put(LANG_FLE, lid);
		langIDs_Desc.put(LANG_FLE_STR, lid);
		
		lid = new LanguageID(LANG_FON, LANG_FON_STR, LANG_FON_ABR);
		langIDs.put(LANG_FON, lid);
		langIDs_Desc.put(LANG_FON_STR, lid);
		
		lid = new LanguageID(LANG_FRA, LANG_FRA_STR, LANG_FRA_ABR);
		langIDs.put(LANG_FRA, lid);
		langIDs_Desc.put(LANG_FRA_STR, lid);
		
		lid = new LanguageID(LANG_FRI, LANG_FRI_STR, LANG_FRI_ABR);
		langIDs.put(LANG_FRI, lid);
		langIDs_Desc.put(LANG_FRI_STR, lid);
		
		lid = new LanguageID(LANG_FRP, LANG_FRP_STR, LANG_FRP_ABR);
		langIDs.put(LANG_FRP, lid);
		langIDs_Desc.put(LANG_FRP_STR, lid);
		
		lid = new LanguageID(LANG_FUL, LANG_FUL_STR, LANG_FUL_ABR);
		langIDs.put(LANG_FUL, lid);
		langIDs_Desc.put(LANG_FUL_STR, lid);
		
		// N.b. The language "ga", not Irish
		lid = new LanguageID(LANG_GA, LANG_GA_STR, LANG_GA_ABR);
		langIDs.put(LANG_GA, lid);
		langIDs_Desc.put(LANG_GA_STR, lid);
				
		lid = new LanguageID(LANG_GAB, LANG_GAB_STR, LANG_GAB_ABR);
		langIDs.put(LANG_GAB, lid);
		langIDs_Desc.put(LANG_GAB_STR, lid);
		
		lid = new LanguageID(LANG_GAG, LANG_GAG_STR, LANG_GAG_ABR);
		langIDs.put(LANG_GAG, lid);
		langIDs_Desc.put(LANG_GAG_STR, lid);
		
		lid = new LanguageID(LANG_GALO, LANG_GALO_STR, LANG_GALO_ABR);
		langIDs.put(LANG_GALO, lid);
		langIDs_Desc.put(LANG_GALO_STR, lid);
		
		lid = new LanguageID(LANG_GAM, LANG_GAM_STR, LANG_GAM_ABR);
		langIDs.put(LANG_GAM, lid);
		langIDs_Desc.put(LANG_GAM_STR, lid);
		
		lid = new LanguageID(LANG_GAN, LANG_GAN_STR, LANG_GAN_ABR);
		langIDs.put(LANG_GAN, lid);
		langIDs_Desc.put(LANG_GAN_STR, lid);
		
		lid = new LanguageID(LANG_GAU, LANG_GAU_STR, LANG_GAU_ABR);
		langIDs.put(LANG_GAU, lid);
		langIDs_Desc.put(LANG_GAU_STR, lid);
		
		lid = new LanguageID(LANG_GAR, LANG_GAR_STR, LANG_GAR_ABR);
		langIDs.put(LANG_GAR, lid);
		langIDs_Desc.put(LANG_GAR_STR, lid);
		
		lid = new LanguageID(LANG_GAS, LANG_GAS_STR, LANG_GAS_ABR);
		langIDs.put(LANG_GAS, lid);
		langIDs_Desc.put(LANG_GAS_STR, lid);
		
		lid = new LanguageID(LANG_GAU, LANG_GAU_STR, LANG_GAU_ABR);
		langIDs.put(LANG_GAU, lid);
		langIDs_Desc.put(LANG_GAU_STR, lid);
		
		lid = new LanguageID(LANG_GAY, LANG_GAY_STR, LANG_GAY_ABR);
		langIDs.put(LANG_GAY, lid);
		langIDs_Desc.put(LANG_GAY_STR, lid);
		
		lid = new LanguageID(LANG_GAZ, LANG_GAZ_STR, LANG_GAZ_ABR);
		langIDs.put(LANG_GAZ, lid);
		langIDs_Desc.put(LANG_GAZ_STR, lid);
		
		lid = new LanguageID(LANG_GEE, LANG_GEE_STR, LANG_GEE_ABR);
		langIDs.put(LANG_GEE, lid);
		langIDs_Desc.put(LANG_GEE_STR, lid);
		
		lid = new LanguageID(LANG_GEO, LANG_GEO_STR, LANG_GEO_ABR);
		langIDs.put(LANG_GEO, lid);
		langIDs_Desc.put(LANG_GEO_STR, lid);
		
		lid = new LanguageID(LANG_GEP, LANG_GEP_STR, LANG_GEP_ABR);
		langIDs.put(LANG_GEP, lid);
		langIDs_Desc.put(LANG_GEP_STR, lid);
		
		lid = new LanguageID(LANG_GHA, LANG_GHA_STR, LANG_GHA_ABR);
		langIDs.put(LANG_GHA, lid);
		langIDs_Desc.put(LANG_GHA_STR, lid);
		
		lid = new LanguageID(LANG_GIL, LANG_GIL_STR, LANG_GIL_ABR);
		langIDs.put(LANG_GIL, lid);
		langIDs_Desc.put(LANG_GIL_STR, lid);
		
		lid = new LanguageID(LANG_GOG, LANG_GOG_STR, LANG_GOG_ABR);
		langIDs.put(LANG_GOG, lid);
		langIDs_Desc.put(LANG_GOG_STR, lid);
		
		lid = new LanguageID(LANG_GOL, LANG_GOL_STR, LANG_GOL_ABR);
		langIDs.put(LANG_GOL, lid);
		langIDs_Desc.put(LANG_GOL_STR, lid);
		
		lid = new LanguageID(LANG_GOO, LANG_GOO_STR, LANG_GOO_ABR);
		langIDs.put(LANG_GOO, lid);
		langIDs_Desc.put(LANG_GOO_STR, lid);
		
		lid = new LanguageID(LANG_GOY, LANG_GOY_STR, LANG_GOY_ABR);
		langIDs.put(LANG_GOY, lid);
		langIDs_Desc.put(LANG_GOY_STR, lid);
		
		lid = new LanguageID(LANG_GRO, LANG_GRO_STR, LANG_GRO_ABR);
		langIDs.put(LANG_GRO, lid);
		langIDs_Desc.put(LANG_GRO_STR, lid);
		
		lid = new LanguageID(LANG_GSL, LANG_GSL_STR, LANG_GSL_ABR);
		langIDs.put(LANG_GSL, lid);
		langIDs_Desc.put(LANG_GSL_STR, lid);
		
		lid = new LanguageID(LANG_GUA, LANG_GUA_STR, LANG_GUA_ABR);
		langIDs.put(LANG_GUA, lid);
		langIDs_Desc.put(LANG_GUA_STR, lid);
		
		lid = new LanguageID(LANG_GUE, LANG_GUE_STR, LANG_GUE_ABR);
		langIDs.put(LANG_GUE, lid);
		langIDs_Desc.put(LANG_GUE_STR, lid);

		lid = new LanguageID(LANG_GUL, LANG_GUL_STR, LANG_GUL_ABR);
		langIDs.put(LANG_GUL, lid);
		langIDs_Desc.put(LANG_GUL_STR, lid);

		lid = new LanguageID(LANG_GUN, LANG_GUN_STR, LANG_GUN_ABR);
		langIDs.put(LANG_GUN, lid);
		langIDs_Desc.put(LANG_GUN_STR, lid);

		lid = new LanguageID(LANG_GUS, LANG_GUS_STR, LANG_GUS_ABR);
		langIDs.put(LANG_GUS, lid);
		langIDs_Desc.put(LANG_GUS_STR, lid);

		lid = new LanguageID(LANG_GUU, LANG_GUU_STR, LANG_GUU_ABR);
		langIDs.put(LANG_GUU, lid);
		langIDs_Desc.put(LANG_GUU_STR, lid);

		lid = new LanguageID(LANG_HAI, LANG_HAI_STR, LANG_HAI_ABR);
		langIDs.put(LANG_HAI, lid);
		langIDs_Desc.put(LANG_HAI_STR, lid);
		
		lid = new LanguageID(LANG_HAK, LANG_HAK_STR, LANG_HAK_ABR);
		langIDs.put(LANG_HAK, lid);
		langIDs_Desc.put(LANG_HAK_STR, lid);
		
		lid = new LanguageID(LANG_HAM, LANG_HAM_STR, LANG_HAM_ABR);
		langIDs.put(LANG_HAM, lid);
		langIDs_Desc.put(LANG_HAM_STR, lid);
		
		lid = new LanguageID(LANG_HAME, LANG_HAME_STR, LANG_HAME_ABR);
		langIDs.put(LANG_HAME, lid);
		langIDs_Desc.put(LANG_HAME_STR, lid);
		
		lid = new LanguageID(LANG_HAR, LANG_HAR_STR, LANG_HAR_ABR);
		langIDs.put(LANG_HAR, lid);
		langIDs_Desc.put(LANG_HAR_STR, lid);
		
		lid = new LanguageID(LANG_HAS, LANG_HAS_STR, LANG_HAS_ABR);
		langIDs.put(LANG_HAS, lid);
		langIDs_Desc.put(LANG_HAS_STR, lid);
		
		lid = new LanguageID(LANG_HC, LANG_HC_STR, LANG_HC_ABR);
		langIDs.put(LANG_HC, lid);
		langIDs_Desc.put(LANG_HC_STR, lid);
		
		lid = new LanguageID(LANG_HIL, LANG_HIL_STR, LANG_HIL_ABR);
		langIDs.put(LANG_HIL, lid);
		langIDs_Desc.put(LANG_HIL_STR, lid);
		
		lid = new LanguageID(LANG_HIR, LANG_HIR_STR, LANG_HIR_ABR);
		langIDs.put(LANG_HIR, lid);
		langIDs_Desc.put(LANG_HIR_STR, lid);
		
		lid = new LanguageID(LANG_HMO, LANG_HMO_STR, LANG_HMO_ABR);
		langIDs.put(LANG_HMO, lid);
		langIDs_Desc.put(LANG_HMO_STR, lid);
		
		lid = new LanguageID(LANG_HMOD, LANG_HMOD_STR, LANG_HMOD_ABR);
		langIDs.put(LANG_HMOD, lid);
		langIDs_Desc.put(LANG_HMOD_STR, lid);
		
		lid = new LanguageID(LANG_HOP, LANG_HOP_STR, LANG_HOP_ABR);
		langIDs.put(LANG_HOP, lid);
		langIDs_Desc.put(LANG_HOP_STR, lid);
		
		lid = new LanguageID(LANG_HUN, LANG_HUN_STR, LANG_HUN_ABR);
		langIDs.put(LANG_HUN, lid);
		langIDs_Desc.put(LANG_HUN_STR, lid);
		
		lid = new LanguageID(LANG_HUR, LANG_HUR_STR, LANG_HUR_ABR);
		langIDs.put(LANG_HUR, lid);
		langIDs_Desc.put(LANG_HUR_STR, lid);
		
		lid = new LanguageID(LANG_HVL, LANG_HVL_STR, LANG_HVL_ABR);
		langIDs.put(LANG_HVL, lid);
		langIDs_Desc.put(LANG_HVL_STR, lid);
		
		lid = new LanguageID(LANG_HW, LANG_HW_STR, LANG_HW_ABR);
		langIDs.put(LANG_HW, lid);
		langIDs_Desc.put(LANG_HW_STR, lid);
		
		lid = new LanguageID(LANG_HWP, LANG_HWP_STR, LANG_HWP_ABR);
		langIDs.put(LANG_HWP, lid);
		langIDs_Desc.put(LANG_HWP_STR, lid);
		
		lid = new LanguageID(LANG_IBA, LANG_IBA_STR, LANG_IBA_ABR);
		langIDs.put(LANG_IBA, lid);
		langIDs_Desc.put(LANG_IBA_STR, lid);
		
		lid = new LanguageID(LANG_IDA, LANG_IDA_STR, LANG_IDA_ABR);
		langIDs.put(LANG_IDA, lid);
		langIDs_Desc.put(LANG_IDA_STR, lid);
		
		lid = new LanguageID(LANG_IGB, LANG_IGB_STR, LANG_IGB_ABR);
		langIDs.put(LANG_IGB, lid);
		langIDs_Desc.put(LANG_IGB_STR, lid);
		
		lid = new LanguageID(LANG_IL, LANG_IL_STR, LANG_IL_ABR);
		langIDs.put(LANG_IL, lid);
		langIDs_Desc.put(LANG_IL_STR, lid);
		
		lid = new LanguageID(LANG_IMN, LANG_IMN_STR, LANG_IMN_ABR);
		langIDs.put(LANG_IMN, lid);
		langIDs_Desc.put(LANG_IMN_STR, lid);
		
		lid = new LanguageID(LANG_INDO, LANG_INDO_STR, LANG_INDO_ABR);
		langIDs.put(LANG_INDO, lid);
		langIDs_Desc.put(LANG_INDO_STR, lid);
		
		lid = new LanguageID(LANG_INE, LANG_INE_STR, LANG_INE_ABR);
		langIDs.put(LANG_INE, lid);
		langIDs_Desc.put(LANG_INE_STR, lid);
		
		lid = new LanguageID(LANG_INGR, LANG_INGR_STR, LANG_INGR_ABR);
		langIDs.put(LANG_INGR, lid);
		langIDs_Desc.put(LANG_INGR_STR, lid);
		
		lid = new LanguageID(LANG_INGU, LANG_INGU_STR, LANG_INGU_ABR);
		langIDs.put(LANG_INGU, lid);
		langIDs_Desc.put(LANG_INGU_STR, lid);
		
		lid = new LanguageID(LANG_INS, LANG_INS_STR, LANG_INS_ABR);
		langIDs.put(LANG_INS, lid);
		langIDs_Desc.put(LANG_INS_STR, lid);
		
		lid = new LanguageID(LANG_IR, LANG_IR_STR, LANG_IR_ABR);
		langIDs.put(LANG_IR, lid);
		langIDs_Desc.put(LANG_IR_STR, lid);
		
		lid = new LanguageID(LANG_IRAN, LANG_IRAN_STR, LANG_IRAN_ABR);
		langIDs.put(LANG_IRAN, lid);
		langIDs_Desc.put(LANG_IRAN_STR, lid);
		
		lid = new LanguageID(LANG_IST, LANG_IST_STR, LANG_IST_ABR);
		langIDs.put(LANG_IST, lid);
		langIDs_Desc.put(LANG_IST_STR, lid);

		lid = new LanguageID(LANG_ITA, LANG_ITA_STR, LANG_ITA_ABR);
		langIDs.put(LANG_ITA, lid);
		langIDs_Desc.put(LANG_ITA_STR, lid);

		lid = new LanguageID(LANG_ITE, LANG_ITE_STR, LANG_ITE_ABR);
		langIDs.put(LANG_ITE, lid);
		langIDs_Desc.put(LANG_ITE_STR, lid);

		lid = new LanguageID(LANG_IZA, LANG_IZA_STR, LANG_IZA_ABR);
		langIDs.put(LANG_IZA, lid);
		langIDs_Desc.put(LANG_IZA_STR, lid);

		lid = new LanguageID(LANG_JAR, LANG_JAR_STR, LANG_JAR_ABR);
		langIDs.put(LANG_JAR, lid);
		langIDs_Desc.put(LANG_JAR_STR, lid);

		lid = new LanguageID(LANG_JAV, LANG_JAV_STR, LANG_JAV_ABR);
		langIDs.put(LANG_JAV, lid);
		langIDs_Desc.put(LANG_JAV_STR, lid);

		lid = new LanguageID(LANG_JC, LANG_JC_STR, LANG_JC_ABR);
		langIDs.put(LANG_JC, lid);
		langIDs_Desc.put(LANG_JC_STR, lid);
		
		lid = new LanguageID(LANG_JE, LANG_JE_STR, LANG_JE_ABR);
		langIDs.put(LANG_JE, lid);
		langIDs_Desc.put(LANG_JE_STR, lid);
		
		lid = new LanguageID(LANG_JIN, LANG_JIN_STR, LANG_JIN_ABR);
		langIDs.put(LANG_JIN, lid);
		langIDs_Desc.put(LANG_JIN_STR, lid);
		
		lid = new LanguageID(LANG_JUR, LANG_JUR_STR, LANG_JUR_ABR);
		langIDs.put(LANG_JUR, lid);
		langIDs_Desc.put(LANG_JUR_STR, lid);
		
		lid = new LanguageID(LANG_KABA, LANG_KABA_STR, LANG_KABA_ABR);
		langIDs.put(LANG_KABA, lid);
		langIDs_Desc.put(LANG_KABA_STR, lid);
		
		lid = new LanguageID(LANG_KABU, LANG_KABU_STR, LANG_KABU_ABR);
		langIDs.put(LANG_KABU, lid);
		langIDs_Desc.put(LANG_KABU_STR, lid);
		
		lid = new LanguageID(LANG_KABY, LANG_KABY_STR, LANG_KABY_ABR);
		langIDs.put(LANG_KABY, lid);
		langIDs_Desc.put(LANG_KABY_STR, lid);
		
		lid = new LanguageID(LANG_KAD, LANG_KAD_STR, LANG_KAD_ABR);
		langIDs.put(LANG_KAD, lid);
		langIDs_Desc.put(LANG_KAD_STR, lid);
		
		lid = new LanguageID(LANG_KALG, LANG_KALG_STR, LANG_KALG_ABR);
		langIDs.put(LANG_KALG, lid);
		langIDs_Desc.put(LANG_KALG_STR, lid);
		
		lid = new LanguageID(LANG_KALI, LANG_KALI_STR, LANG_KALI_ABR);
		langIDs.put(LANG_KALI, lid);
		langIDs_Desc.put(LANG_KALI_STR, lid);
		
		lid = new LanguageID(LANG_KALM, LANG_KALM_STR, LANG_KALM_ABR);
		langIDs.put(LANG_KALM, lid);
		langIDs_Desc.put(LANG_KALM_STR, lid);
		
		lid = new LanguageID(LANG_KALN, LANG_KALN_STR, LANG_KALN_ABR);
		langIDs.put(LANG_KALN, lid);
		langIDs_Desc.put(LANG_KALN_STR, lid);
		
		lid = new LanguageID(LANG_KALY, LANG_KALY_STR, LANG_KALY_ABR);
		langIDs.put(LANG_KALY, lid);
		langIDs_Desc.put(LANG_KALY_STR, lid);
		
		lid = new LanguageID(LANG_KAMA, LANG_KAMA_STR, LANG_KAMA_ABR);
		langIDs.put(LANG_KAMA, lid);
		langIDs_Desc.put(LANG_KAMA_STR, lid);
		
		lid = new LanguageID(LANG_KAMB, LANG_KAMB_STR, LANG_KAMB_ABR);
		langIDs.put(LANG_KAMB, lid);
		langIDs_Desc.put(LANG_KAMB_STR, lid);
		
		lid = new LanguageID(LANG_KANJ, LANG_KANJ_STR, LANG_KANJ_ABR);
		langIDs.put(LANG_KANJ, lid);
		langIDs_Desc.put(LANG_KANJ_STR, lid);
		
		lid = new LanguageID(LANG_KANU, LANG_KANU_STR, LANG_KANU_ABR);
		langIDs.put(LANG_KANU, lid);
		langIDs_Desc.put(LANG_KANU_STR, lid);
		
		lid = new LanguageID(LANG_KAPA, LANG_KAPA_STR, LANG_KAPA_ABR);
		langIDs.put(LANG_KAPA, lid);
		langIDs_Desc.put(LANG_KAPA_STR, lid);
		
		lid = new LanguageID(LANG_KAPI, LANG_KAPI_STR, LANG_KAPI_ABR);
		langIDs.put(LANG_KAPI, lid);
		langIDs_Desc.put(LANG_KAPI_STR, lid);
		
		lid = new LanguageID(LANG_KARA, LANG_KARA_STR, LANG_KARA_ABR);
		langIDs.put(LANG_KARA, lid);
		langIDs_Desc.put(LANG_KARA_STR, lid);
		
		lid = new LanguageID(LANG_KARE, LANG_KARE_STR, LANG_KARE_ABR);
		langIDs.put(LANG_KARE, lid);
		langIDs_Desc.put(LANG_KARE_STR, lid);
		
		lid = new LanguageID(LANG_KARI, LANG_KARI_STR, LANG_KARI_ABR);
		langIDs.put(LANG_KARI, lid);
		langIDs_Desc.put(LANG_KARI_STR, lid);
		
		lid = new LanguageID(LANG_KARK, LANG_KARK_STR, LANG_KARK_ABR);
		langIDs.put(LANG_KARK, lid);
		langIDs_Desc.put(LANG_KARK_STR, lid);
		
		lid = new LanguageID(LANG_KARM, LANG_KARM_STR, LANG_KARM_ABR);
		langIDs.put(LANG_KARM, lid);
		langIDs_Desc.put(LANG_KARM_STR, lid);
		
		lid = new LanguageID(LANG_KAS, LANG_KAS_STR, LANG_KAS_ABR);
		langIDs.put(LANG_KAS, lid);
		langIDs_Desc.put(LANG_KAS_STR, lid);
		
		lid = new LanguageID(LANG_KAU, LANG_KAU_STR, LANG_KAU_ABR);
		langIDs.put(LANG_KAU, lid);
		langIDs_Desc.put(LANG_KAU_STR, lid);
		
		lid = new LanguageID(LANG_KEL, LANG_KEL_STR, LANG_KEL_ABR);
		langIDs.put(LANG_KEL, lid);
		langIDs_Desc.put(LANG_KEL_STR, lid);
		
		lid = new LanguageID(LANG_KEM, LANG_KEM_STR, LANG_KEM_ABR);
		langIDs.put(LANG_KEM, lid);
		langIDs_Desc.put(LANG_KEM_STR, lid);
		
		lid = new LanguageID(LANG_KESA, LANG_KESA_STR, LANG_KESA_ABR);
		langIDs.put(LANG_KESA, lid);
		langIDs_Desc.put(LANG_KESA_STR, lid);
		
		lid = new LanguageID(LANG_KET, LANG_KET_STR, LANG_KET_ABR);
		langIDs.put(LANG_KET, lid);
		langIDs_Desc.put(LANG_KET_STR, lid);
		
		lid = new LanguageID(LANG_KFR, LANG_KFR_STR, LANG_KFR_ABR);
		langIDs.put(LANG_KFR, lid);
		langIDs_Desc.put(LANG_KFR_STR, lid);
		
		lid = new LanguageID(LANG_KHAK, LANG_KHAK_STR, LANG_KHAK_ABR);
		langIDs.put(LANG_KHAK, lid);
		langIDs_Desc.put(LANG_KHAK_STR, lid);
		
		lid = new LanguageID(LANG_KHAN, LANG_KHAN_STR, LANG_KHAN_ABR);
		langIDs.put(LANG_KHAN, lid);
		langIDs_Desc.put(LANG_KHAN_STR, lid);
		
		lid = new LanguageID(LANG_KHAZ, LANG_KHAZ_STR, LANG_KHAZ_ABR);
		langIDs.put(LANG_KHAZ, lid);
		langIDs_Desc.put(LANG_KHAZ_STR, lid);
		
		lid = new LanguageID(LANG_KHI, LANG_KHI_STR, LANG_KHI_ABR);
		langIDs.put(LANG_KHI, lid);
		langIDs_Desc.put(LANG_KHI_STR, lid);
		
		lid = new LanguageID(LANG_KHME, LANG_KHME_STR, LANG_KHME_ABR);
		langIDs.put(LANG_KHME, lid);
		langIDs_Desc.put(LANG_KHME_STR, lid);
		
		lid = new LanguageID(LANG_KHMU, LANG_KHMU_STR, LANG_KHMU_ABR);
		langIDs.put(LANG_KHMU, lid);
		langIDs_Desc.put(LANG_KHMU_STR, lid);
		
		lid = new LanguageID(LANG_KHO, LANG_KHO_STR, LANG_KHO_ABR);
		langIDs.put(LANG_KHO, lid);
		langIDs_Desc.put(LANG_KHO_STR, lid);
		
		lid = new LanguageID(LANG_KHV, LANG_KHV_STR, LANG_KHV_ABR);
		langIDs.put(LANG_KHV, lid);
		langIDs_Desc.put(LANG_KHV_STR, lid);
		
		lid = new LanguageID(LANG_KIC, LANG_KIC_STR, LANG_KIC_ABR);
		langIDs.put(LANG_KIC, lid);
		langIDs_Desc.put(LANG_KIC_STR, lid);
		
		lid = new LanguageID(LANG_KIK, LANG_KIK_STR, LANG_KIK_ABR);
		langIDs.put(LANG_KIK, lid);
		langIDs_Desc.put(LANG_KIK_STR, lid);
		
		lid = new LanguageID(LANG_KIL, LANG_KIL_STR, LANG_KIL_ABR);
		langIDs.put(LANG_KIL, lid);
		langIDs_Desc.put(LANG_KIL_STR, lid);
		
		lid = new LanguageID(LANG_KIN, LANG_KIN_STR, LANG_KIN_ABR);
		langIDs.put(LANG_KIN, lid);
		langIDs_Desc.put(LANG_KIN_STR, lid);
		
		lid = new LanguageID(LANG_KIP, LANG_KIP_STR, LANG_KIP_ABR);
		langIDs.put(LANG_KIP, lid);
		langIDs_Desc.put(LANG_KIP_STR, lid);
		
		lid = new LanguageID(LANG_KISA, LANG_KISA_STR, LANG_KISA_ABR);
		langIDs.put(LANG_KISA, lid);
		langIDs_Desc.put(LANG_KISA_STR, lid);
		
		lid = new LanguageID(LANG_KNG, LANG_KNG_STR, LANG_KNG_ABR);
		langIDs.put(LANG_KNG, lid);
		langIDs_Desc.put(LANG_KNG_STR, lid);
		
		lid = new LanguageID(LANG_KOL, LANG_KOL_STR, LANG_KOL_ABR);
		langIDs.put(LANG_KOL, lid);
		langIDs_Desc.put(LANG_KOL_STR, lid);
		
		lid = new LanguageID(LANG_KOM, LANG_KOM_STR, LANG_KOM_ABR);
		langIDs.put(LANG_KOM, lid);
		langIDs_Desc.put(LANG_KOM_STR, lid);
		
		lid = new LanguageID(LANG_KON, LANG_KON_STR, LANG_KON_ABR);
		langIDs.put(LANG_KON, lid);
		langIDs_Desc.put(LANG_KON_STR, lid);
		
		lid = new LanguageID(LANG_KOPE, LANG_KOPE_STR, LANG_KOPE_ABR);
		langIDs.put(LANG_KOPE, lid);
		langIDs_Desc.put(LANG_KOPE_STR, lid);
		
		lid = new LanguageID(LANG_KORE, LANG_KORE_STR, LANG_KORE_ABR);
		langIDs.put(LANG_KORE, lid);
		langIDs_Desc.put(LANG_KORE_STR, lid);
		
		lid = new LanguageID(LANG_KORY, LANG_KORY_STR, LANG_KORY_ABR);
		langIDs.put(LANG_KORY, lid);
		langIDs_Desc.put(LANG_KORY_STR, lid);
		
		lid = new LanguageID(LANG_KOT, LANG_KOT_STR, LANG_KOT_ABR);
		langIDs.put(LANG_KOT, lid);
		langIDs_Desc.put(LANG_KOT_STR, lid);
		
		lid = new LanguageID(LANG_KOZY, LANG_KOZY_STR, LANG_KOZY_ABR);
		langIDs.put(LANG_KOZY, lid);
		langIDs_Desc.put(LANG_KOZY_STR, lid);
		
		lid = new LanguageID(LANG_KRIL, LANG_KRIL_STR, LANG_KRIL_ABR);
		langIDs.put(LANG_KRIL, lid);
		langIDs_Desc.put(LANG_KRIL_STR, lid);
		
		lid = new LanguageID(LANG_KRIO, LANG_KRIO_STR, LANG_KRIO_ABR);
		langIDs.put(LANG_KRIO, lid);
		langIDs_Desc.put(LANG_KRIO_STR, lid);
		
		lid = new LanguageID(LANG_KRS, LANG_KRS_STR, LANG_KRS_ABR);
		langIDs.put(LANG_KRS, lid);
		langIDs_Desc.put(LANG_KRS_STR, lid);
		
		lid = new LanguageID(LANG_KUA, LANG_KUA_STR, LANG_KUA_ABR);
		langIDs.put(LANG_KUA, lid);
		langIDs_Desc.put(LANG_KUA_STR, lid);
		
		lid = new LanguageID(LANG_KUM, LANG_KUM_STR, LANG_KUM_ABR);
		langIDs.put(LANG_KUM, lid);
		langIDs_Desc.put(LANG_KUM_STR, lid);
		
		lid = new LanguageID(LANG_KUN, LANG_KUN_STR, LANG_KUN_ABR);
		langIDs.put(LANG_KUN, lid);
		langIDs_Desc.put(LANG_KUN_STR, lid);
		
		lid = new LanguageID(LANG_KUR, LANG_KUR_STR, LANG_KUR_ABR);
		langIDs.put(LANG_KUR, lid);
		langIDs_Desc.put(LANG_KUR_STR, lid);
		
		lid = new LanguageID(LANG_KUS, LANG_KUS_STR, LANG_KUS_ABR);
		langIDs.put(LANG_KUS, lid);
		langIDs_Desc.put(LANG_KUS_STR, lid);
		
		lid = new LanguageID(LANG_KUV, LANG_KUV_STR, LANG_KUV_ABR);
		langIDs.put(LANG_KUV, lid);
		langIDs_Desc.put(LANG_KUV_STR, lid);
		
		lid = new LanguageID(LANG_KV, LANG_KV_STR, LANG_KV_ABR);
		langIDs.put(LANG_KV, lid);
		langIDs_Desc.put(LANG_KV_STR, lid);
		
		lid = new LanguageID(LANG_KWA, LANG_KWA_STR, LANG_KWA_ABR);
		langIDs.put(LANG_KWA, lid);
		langIDs_Desc.put(LANG_KWA_STR, lid);
		
		lid = new LanguageID(LANG_KYR, LANG_KYR_STR, LANG_KYR_ABR);
		langIDs.put(LANG_KYR, lid);
		langIDs_Desc.put(LANG_KYR_STR, lid);
		
		lid = new LanguageID(LANG_LAA, LANG_LAA_STR, LANG_LAA_ABR);
		langIDs.put(LANG_LAA, lid);
		langIDs_Desc.put(LANG_LAA_STR, lid);
		
		lid = new LanguageID(LANG_LAD, LANG_LAD_STR, LANG_LAD_ABR);
		langIDs.put(LANG_LAD, lid);
		langIDs_Desc.put(LANG_LAD_STR, lid);
		
		lid = new LanguageID(LANG_LAK, LANG_LAK_STR, LANG_LAK_ABR);
		langIDs.put(LANG_LAK, lid);
		langIDs_Desc.put(LANG_LAK_STR, lid);
		
		lid = new LanguageID(LANG_LAKI, LANG_LAKI_STR, LANG_LAKI_ABR);
		langIDs.put(LANG_LAKI, lid);
		langIDs_Desc.put(LANG_LAKI_STR, lid);
		
		lid = new LanguageID(LANG_LAKO, LANG_LAKO_STR, LANG_LAKO_ABR);
		langIDs.put(LANG_LAKO, lid);
		langIDs_Desc.put(LANG_LAKO_STR, lid);
		
		lid = new LanguageID(LANG_LAT, LANG_LAT_STR, LANG_LAT_ABR);
		langIDs.put(LANG_LAT, lid);
		langIDs_Desc.put(LANG_LAT_STR, lid);
		
		lid = new LanguageID(LANG_LAU, LANG_LAU_STR, LANG_LAU_ABR);
		langIDs.put(LANG_LAU, lid);
		langIDs_Desc.put(LANG_LAU_STR, lid);
		
		lid = new LanguageID(LANG_LAV, LANG_LAV_STR, LANG_LAV_ABR);
		langIDs.put(LANG_LAV, lid);
		langIDs_Desc.put(LANG_LAV_STR, lid);
		
		lid = new LanguageID(LANG_LAZ, LANG_LAZ_STR, LANG_LAZ_ABR);
		langIDs.put(LANG_LAZ, lid);
		langIDs_Desc.put(LANG_LAZ_STR, lid);
		
		lid = new LanguageID(LANG_LCI, LANG_LCI_STR, LANG_LCI_ABR);
		langIDs.put(LANG_LCI, lid);
		langIDs_Desc.put(LANG_LCI_STR, lid);
		
		lid = new LanguageID(LANG_LCR, LANG_LCR_STR, LANG_LCR_ABR);
		langIDs.put(LANG_LCR, lid);
		langIDs_Desc.put(LANG_LCR_STR, lid);
		
		lid = new LanguageID(LANG_LDI, LANG_LDI_STR, LANG_LDI_ABR);
		langIDs.put(LANG_LDI, lid);
		langIDs_Desc.put(LANG_LDI_STR, lid);
		
		lid = new LanguageID(LANG_LEM, LANG_LEM_STR, LANG_LEM_ABR);
		langIDs.put(LANG_LEM, lid);
		langIDs_Desc.put(LANG_LEM_STR, lid);
		
		lid = new LanguageID(LANG_LEN, LANG_LEN_STR, LANG_LEN_ABR);
		langIDs.put(LANG_LEN, lid);
		langIDs_Desc.put(LANG_LEN_STR, lid);
		
		lid = new LanguageID(LANG_LEO, LANG_LEO_STR, LANG_LEO_ABR);
		langIDs.put(LANG_LEO, lid);
		langIDs_Desc.put(LANG_LEO_STR, lid);
		
		lid = new LanguageID(LANG_LEP, LANG_LEP_STR, LANG_LEP_ABR);
		langIDs.put(LANG_LEP, lid);
		langIDs_Desc.put(LANG_LEP_STR, lid);
		
		lid = new LanguageID(LANG_LEZ, LANG_LEZ_STR, LANG_LEZ_ABR);
		langIDs.put(LANG_LEZ, lid);
		langIDs_Desc.put(LANG_LEZ_STR, lid);
		
		lid = new LanguageID(LANG_LIA, LANG_LIA_STR, LANG_LIA_ABR);
		langIDs.put(LANG_LIA, lid);
		langIDs_Desc.put(LANG_LIA_STR, lid);
		
		lid = new LanguageID(LANG_LIM, LANG_LIM_STR, LANG_LIM_ABR);
		langIDs.put(LANG_LIM, lid);
		langIDs_Desc.put(LANG_LIM_STR, lid);
		
		lid = new LanguageID(LANG_LIV, LANG_LIV_STR, LANG_LIV_ABR);
		langIDs.put(LANG_LIV, lid);
		langIDs_Desc.put(LANG_LIV_STR, lid);
		
		lid = new LanguageID(LANG_LM, LANG_LM_STR, LANG_LM_ABR);
		langIDs.put(LANG_LM, lid);
		langIDs_Desc.put(LANG_LM_STR, lid);
		
		lid = new LanguageID(LANG_LOM, LANG_LOM_STR, LANG_LOM_ABR);
		langIDs.put(LANG_LOM, lid);
		langIDs_Desc.put(LANG_LOM_STR, lid);
		
		lid = new LanguageID(LANG_LOZ, LANG_LOZ_STR, LANG_LOZ_ABR);
		langIDs.put(LANG_LOZ, lid);
		langIDs_Desc.put(LANG_LOZ_STR, lid);
		
		lid = new LanguageID(LANG_LSA, LANG_LSA_STR, LANG_LSA_ABR);
		langIDs.put(LANG_LSA, lid);
		langIDs_Desc.put(LANG_LSA_STR, lid);
		
		lid = new LanguageID(LANG_LSO, LANG_LSO_STR, LANG_LSO_ABR);
		langIDs.put(LANG_LSO, lid);
		langIDs_Desc.put(LANG_LSO_STR, lid);
		
		lid = new LanguageID(LANG_LUG, LANG_LUG_STR, LANG_LUG_ABR);
		langIDs.put(LANG_LUG, lid);
		langIDs_Desc.put(LANG_LUG_STR, lid);

		lid = new LanguageID(LANG_LUH, LANG_LUH_STR, LANG_LUH_ABR);
		langIDs.put(LANG_LUH, lid);
		langIDs_Desc.put(LANG_LUH_STR, lid);

		lid = new LanguageID(LANG_LUKA, LANG_LUKA_STR, LANG_LUKA_ABR);
		langIDs.put(LANG_LUKA, lid);
		langIDs_Desc.put(LANG_LUKA_STR, lid);

		lid = new LanguageID(LANG_LUKT, LANG_LUKT_STR, LANG_LUKT_ABR);
		langIDs.put(LANG_LUKT, lid);
		langIDs_Desc.put(LANG_LUKT_STR, lid);

		lid = new LanguageID(LANG_LUN, LANG_LUN_STR, LANG_LUN_ABR);
		langIDs.put(LANG_LUN, lid);
		langIDs_Desc.put(LANG_LUN_STR, lid);

		lid = new LanguageID(LANG_LUO, LANG_LUO_STR, LANG_LUO_ABR);
		langIDs.put(LANG_LUO, lid);
		langIDs_Desc.put(LANG_LUO_STR, lid);

		lid = new LanguageID(LANG_LUSA, LANG_LUSA_STR, LANG_LUSA_ABR);
		langIDs.put(LANG_LUSA, lid);
		langIDs_Desc.put(LANG_LUSA_STR, lid);
		
		lid = new LanguageID(LANG_LUSI, LANG_LUSI_STR, LANG_LUSI_ABR);
		langIDs.put(LANG_LUSI, lid);
		langIDs_Desc.put(LANG_LUSI_STR, lid);
		
		lid = new LanguageID(LANG_LUW, LANG_LUW_STR, LANG_LUW_ABR);
		langIDs.put(LANG_LUW, lid);
		langIDs_Desc.put(LANG_LUW_STR, lid);
		
		lid = new LanguageID(LANG_LYC, LANG_LYC_STR, LANG_LYC_ABR);
		langIDs.put(LANG_LYC, lid);
		langIDs_Desc.put(LANG_LYC_STR, lid);
		
		lid = new LanguageID(LANG_LYD, LANG_LYD_STR, LANG_LYD_ABR);
		langIDs.put(LANG_LYD, lid);
		langIDs_Desc.put(LANG_LYD_STR, lid);
		
		lid = new LanguageID(LANG_MAAS, LANG_MAAS_STR, LANG_MAAS_ABR);
		langIDs.put(LANG_MAAS, lid);
		langIDs_Desc.put(LANG_MAAS_STR, lid);
		
		lid = new LanguageID(LANG_MAAY, LANG_MAAY_STR, LANG_MAAY_ABR);
		langIDs.put(LANG_MAAY, lid);
		langIDs_Desc.put(LANG_MAAY_STR, lid);
		
		lid = new LanguageID(LANG_MAC, LANG_MAC_STR, LANG_MAC_ABR);
		langIDs.put(LANG_MAC, lid);
		langIDs_Desc.put(LANG_MAC_STR, lid);
		
		lid = new LanguageID(LANG_MAD, LANG_MAD_STR, LANG_MAD_ABR);
		langIDs.put(LANG_MAD, lid);
		langIDs_Desc.put(LANG_MAD_STR, lid);
		
		lid = new LanguageID(LANG_MAKA, LANG_MAKA_STR, LANG_MAKA_ABR);
		langIDs.put(LANG_MAKA, lid);
		langIDs_Desc.put(LANG_MAKA_STR, lid);
		
		lid = new LanguageID(LANG_MAKH, LANG_MAKH_STR, LANG_MAKH_ABR);
		langIDs.put(LANG_MAKH, lid);
		langIDs_Desc.put(LANG_MAKH_STR, lid);
		
		lid = new LanguageID(LANG_MAKS, LANG_MAKS_STR, LANG_MAKS_ABR);
		langIDs.put(LANG_MAKS, lid);
		langIDs_Desc.put(LANG_MAKS_STR, lid);
		
		lid = new LanguageID(LANG_MALC, LANG_MALC_STR, LANG_MALC_ABR);
		langIDs.put(LANG_MALC, lid);
		langIDs_Desc.put(LANG_MALC_STR, lid);
		
		lid = new LanguageID(LANG_MALE, LANG_MALE_STR, LANG_MALE_ABR);
		langIDs.put(LANG_MALE, lid);
		langIDs_Desc.put(LANG_MALE_STR, lid);
		
		lid = new LanguageID(LANG_MALE, LANG_MALE_STR, LANG_MALE_ABR);
		langIDs.put(LANG_MALE, lid);
		langIDs_Desc.put(LANG_MALE_STR, lid);
		
		lid = new LanguageID(LANG_MALT, LANG_MALT_STR, LANG_MALT_ABR);
		langIDs.put(LANG_MALT, lid);
		langIDs_Desc.put(LANG_MALT_STR, lid);
		
		lid = new LanguageID(LANG_MANC, LANG_MANC_STR, LANG_MANC_ABR);
		langIDs.put(LANG_MANC, lid);
		langIDs_Desc.put(LANG_MANC_STR, lid);
		
		lid = new LanguageID(LANG_MAND, LANG_MAND_STR, LANG_MAND_ABR);
		langIDs.put(LANG_MAND, lid);
		langIDs_Desc.put(LANG_MAND_STR, lid);
		
		lid = new LanguageID(LANG_MANG, LANG_MANG_STR, LANG_MANG_ABR);
		langIDs.put(LANG_MANG, lid);
		langIDs_Desc.put(LANG_MANG_STR, lid);
		
		lid = new LanguageID(LANG_MANI, LANG_MANI_STR, LANG_MANI_ABR);
		langIDs.put(LANG_MANI, lid);
		langIDs_Desc.put(LANG_MANI_STR, lid);
		
		lid = new LanguageID(LANG_MANR, LANG_MANR_STR, LANG_MANR_ABR);
		langIDs.put(LANG_MANR, lid);
		langIDs_Desc.put(LANG_MANR_STR, lid);
		
		lid = new LanguageID(LANG_MANS, LANG_MANS_STR, LANG_MANS_ABR);
		langIDs.put(LANG_MANS, lid);
		langIDs_Desc.put(LANG_MANS_STR, lid);
		
		lid = new LanguageID(LANG_MAP, LANG_MAP_STR, LANG_MAP_ABR);
		langIDs.put(LANG_MAP, lid);
		langIDs_Desc.put(LANG_MAP_STR, lid);
		
		lid = new LanguageID(LANG_MARA, LANG_MARA_STR, LANG_MARA_ABR);
		langIDs.put(LANG_MARA, lid);
		langIDs_Desc.put(LANG_MARA_STR, lid);
		
		lid = new LanguageID(LANG_MARC, LANG_MARC_STR, LANG_MARC_ABR);
		langIDs.put(LANG_MARC, lid);
		langIDs_Desc.put(LANG_MARC_STR, lid);
		
		lid = new LanguageID(LANG_MARI, LANG_MARI_STR, LANG_MARI_ABR);
		langIDs.put(LANG_MARI, lid);
		langIDs_Desc.put(LANG_MARI_STR, lid);
		
		lid = new LanguageID(LANG_MARK, LANG_MARK_STR, LANG_MARK_ABR);
		langIDs.put(LANG_MARK, lid);
		langIDs_Desc.put(LANG_MARK_STR, lid);
		
		lid = new LanguageID(LANG_MARO, LANG_MARO_STR, LANG_MARO_ABR);
		langIDs.put(LANG_MARO, lid);
		langIDs_Desc.put(LANG_MARO_STR, lid);
		
		lid = new LanguageID(LANG_MARR, LANG_MARR_STR, LANG_MARR_ABR);
		langIDs.put(LANG_MARR, lid);
		langIDs_Desc.put(LANG_MARR_STR, lid);
		
		lid = new LanguageID(LANG_MARS, LANG_MARS_STR, LANG_MARS_ABR);
		langIDs.put(LANG_MARS, lid);
		langIDs_Desc.put(LANG_MARS_STR, lid);
		
		lid = new LanguageID(LANG_MART, LANG_MART_STR, LANG_MART_ABR);
		langIDs.put(LANG_MART, lid);
		langIDs_Desc.put(LANG_MART_STR, lid);
		
		lid = new LanguageID(LANG_MARW, LANG_MARW_STR, LANG_MARW_ABR);
		langIDs.put(LANG_MARW, lid);
		langIDs_Desc.put(LANG_MARW_STR, lid);
		
		lid = new LanguageID(LANG_MAS, LANG_MAS_STR, LANG_MAS_ABR);
		langIDs.put(LANG_MAS, lid);
		langIDs_Desc.put(LANG_MAS_STR, lid);
		
		lid = new LanguageID(LANG_MAY, LANG_MAY_STR, LANG_MAY_ABR);
		langIDs.put(LANG_MAY, lid);
		langIDs_Desc.put(LANG_MAY_STR, lid);
		
		lid = new LanguageID(LANG_MAZ, LANG_MAZ_STR, LANG_MAZ_ABR);
		langIDs.put(LANG_MAZ, lid);
		langIDs_Desc.put(LANG_MAZ_STR, lid);
		
		lid = new LanguageID(LANG_MBA, LANG_MBA_STR, LANG_MBA_ABR);
		langIDs.put(LANG_MBA, lid);
		langIDs_Desc.put(LANG_MBA_STR, lid);
		
		lid = new LanguageID(LANG_MEA, LANG_MEA_STR, LANG_MEA_ABR);
		langIDs.put(LANG_MEA, lid);
		langIDs_Desc.put(LANG_MEA_STR, lid);
		
		lid = new LanguageID(LANG_MED, LANG_MED_STR, LANG_MED_ABR);
		langIDs.put(LANG_MED, lid);
		langIDs_Desc.put(LANG_MED_STR, lid);
		
		lid = new LanguageID(LANG_MEG, LANG_MEG_STR, LANG_MEG_ABR);
		langIDs.put(LANG_MEG, lid);
		langIDs_Desc.put(LANG_MEG_STR, lid);
		
		lid = new LanguageID(LANG_MEN, LANG_MEN_STR, LANG_MEN_ABR);
		langIDs.put(LANG_MEN, lid);
		langIDs_Desc.put(LANG_MEN_STR, lid);
		
		lid = new LanguageID(LANG_MERI, LANG_MERI_STR, LANG_MERI_ABR);
		langIDs.put(LANG_MERI, lid);
		langIDs_Desc.put(LANG_MERI_STR, lid);
		
		lid = new LanguageID(LANG_MERU, LANG_MERU_STR, LANG_MERU_ABR);
		langIDs.put(LANG_MERU, lid);
		langIDs_Desc.put(LANG_MERU_STR, lid);
		
		lid = new LanguageID(LANG_MIA, LANG_MIA_STR, LANG_MIA_ABR);
		langIDs.put(LANG_MIA, lid);
		langIDs_Desc.put(LANG_MIA_STR, lid);
		
		lid = new LanguageID(LANG_MIC, LANG_MIC_STR, LANG_MIC_ABR);
		langIDs.put(LANG_MIC, lid);
		langIDs_Desc.put(LANG_MIC_STR, lid);
		
		lid = new LanguageID(LANG_MIKM, LANG_MIKM_STR, LANG_MIKM_ABR);
		langIDs.put(LANG_MIKM, lid);
		langIDs_Desc.put(LANG_MIKM_STR, lid);
		
		lid = new LanguageID(LANG_MINA, LANG_MINA_STR, LANG_MINA_ABR);
		langIDs.put(LANG_MINA, lid);
		langIDs_Desc.put(LANG_MINA_STR, lid);
		
		lid = new LanguageID(LANG_MIND, LANG_MIND_STR, LANG_MIND_ABR);
		langIDs.put(LANG_MIND, lid);
		langIDs_Desc.put(LANG_MIND_STR, lid);
		
		lid = new LanguageID(LANG_MING, LANG_MING_STR, LANG_MING_ABR);
		langIDs.put(LANG_MING, lid);
		langIDs_Desc.put(LANG_MING_STR, lid);
		
		lid = new LanguageID(LANG_MINO, LANG_MINO_STR, LANG_MINO_ABR);
		langIDs.put(LANG_MINO, lid);
		langIDs_Desc.put(LANG_MINO_STR, lid);
		
		lid = new LanguageID(LANG_MIRA, LANG_MIRA_STR, LANG_MIRA_ABR);
		langIDs.put(LANG_MIRA, lid);
		langIDs_Desc.put(LANG_MIRA_STR, lid);
		
		lid = new LanguageID(LANG_MIS, LANG_MIS_STR, LANG_MIS_ABR);
		langIDs.put(LANG_MIS, lid);
		langIDs_Desc.put(LANG_MIS_STR, lid);
		
		lid = new LanguageID(LANG_MIY, LANG_MIY_STR, LANG_MIY_ABR);
		langIDs.put(LANG_MIY, lid);
		langIDs_Desc.put(LANG_MIY_STR, lid);
		
		lid = new LanguageID(LANG_MIZO, LANG_MIZO_STR, LANG_MIZO_ABR);
		langIDs.put(LANG_MIZO, lid);
		langIDs_Desc.put(LANG_MIZO_STR, lid);
		
		lid = new LanguageID(LANG_MKE, LANG_MKE_STR, LANG_MKE_ABR);
		langIDs.put(LANG_MKE, lid);
		langIDs_Desc.put(LANG_MKE_STR, lid);
		
		lid = new LanguageID(LANG_MOA, LANG_MOA_STR, LANG_MOA_ABR);
		langIDs.put(LANG_MOA, lid);
		langIDs_Desc.put(LANG_MOA_STR, lid);
		
		lid = new LanguageID(LANG_MOB, LANG_MOB_STR, LANG_MOB_ABR);
		langIDs.put(LANG_MOB, lid);
		langIDs_Desc.put(LANG_MOB_STR, lid);
		
		lid = new LanguageID(LANG_MOH, LANG_MOH_STR, LANG_MOH_ABR);
		langIDs.put(LANG_MOH, lid);
		langIDs_Desc.put(LANG_MOH_STR, lid);
		
		lid = new LanguageID(LANG_MOK, LANG_MOK_STR, LANG_MOK_ABR);
		langIDs.put(LANG_MOK, lid);
		langIDs_Desc.put(LANG_MOK_STR, lid);
		
		lid = new LanguageID(LANG_MOKSH, LANG_MOKSH_STR, LANG_MOKSH_ABR);
		langIDs.put(LANG_MOKSH, lid);
		langIDs_Desc.put(LANG_MOKSH_STR, lid);
		
		lid = new LanguageID(LANG_MON, LANG_MON_STR, LANG_MON_ABR);
		langIDs.put(LANG_MON, lid);
		langIDs_Desc.put(LANG_MON_STR, lid);
		
		lid = new LanguageID(LANG_MONG, LANG_MONG_STR, LANG_MONG_ABR);
		langIDs.put(LANG_MONG, lid);
		langIDs_Desc.put(LANG_MONG_STR, lid);
		
		lid = new LanguageID(LANG_MONT, LANG_MONT_STR, LANG_MONT_ABR);
		langIDs.put(LANG_MONT, lid);
		langIDs_Desc.put(LANG_MONT_STR, lid);
		
		lid = new LanguageID(LANG_MOP, LANG_MOP_STR, LANG_MOP_ABR);
		langIDs.put(LANG_MOP, lid);
		langIDs_Desc.put(LANG_MOP_STR, lid);
		
		lid = new LanguageID(LANG_MORA, LANG_MORA_STR, LANG_MORA_ABR);
		langIDs.put(LANG_MORA, lid);
		langIDs_Desc.put(LANG_MORA_STR, lid);
		
		lid = new LanguageID(LANG_MOT, LANG_MOT_STR, LANG_MOT_ABR);
		langIDs.put(LANG_MOT, lid);
		langIDs_Desc.put(LANG_MOT_STR, lid);
		
		lid = new LanguageID(LANG_MUD, LANG_MUD_STR, LANG_MUD_ABR);
		langIDs.put(LANG_MUD, lid);
		langIDs_Desc.put(LANG_MUD_STR, lid);
		
		lid = new LanguageID(LANG_MUGA, LANG_MUGA_STR, LANG_MUGA_ABR);
		langIDs.put(LANG_MUGA, lid);
		langIDs_Desc.put(LANG_MUGA_STR, lid);
		
		lid = new LanguageID(LANG_MUGG, LANG_MUGG_STR, LANG_MUGG_ABR);
		langIDs.put(LANG_MUGG, lid);
		langIDs_Desc.put(LANG_MUGG_STR, lid);
		
		lid = new LanguageID(LANG_MUR, LANG_MUR_STR, LANG_MUR_ABR);
		langIDs.put(LANG_MUR, lid);
		langIDs_Desc.put(LANG_MUR_STR, lid);
		
		lid = new LanguageID(LANG_MYG, LANG_MYG_STR, LANG_MYG_ABR);
		langIDs.put(LANG_MYG, lid);
		langIDs_Desc.put(LANG_MYG_STR, lid);
		
		lid = new LanguageID(LANG_NAG, LANG_NAG_STR, LANG_NAG_ABR);
		langIDs.put(LANG_NAG, lid);
		langIDs_Desc.put(LANG_NAG_STR, lid);
		
		lid = new LanguageID(LANG_NDAL, LANG_NDAL_STR, LANG_NDAL_ABR);
		langIDs.put(LANG_NDAL, lid);
		langIDs_Desc.put(LANG_NDAL_STR, lid);
		
		lid = new LanguageID(LANG_NDO, LANG_NDO_STR, LANG_NDO_ABR);
		langIDs.put(LANG_NDO, lid);
		langIDs_Desc.put(LANG_NDO_STR, lid);
		
		lid = new LanguageID(LANG_NEA, LANG_NEA_STR, LANG_NEA_ABR);
		langIDs.put(LANG_NEA, lid);
		langIDs_Desc.put(LANG_NEA_STR, lid);
		
		lid = new LanguageID(LANG_NEN, LANG_NEN_STR, LANG_NEN_ABR);
		langIDs.put(LANG_NEN, lid);
		langIDs_Desc.put(LANG_NEN_STR, lid);
		
		lid = new LanguageID(LANG_NEW, LANG_NEW_STR, LANG_NEW_ABR);
		langIDs.put(LANG_NEW, lid);
		langIDs_Desc.put(LANG_NEW_STR, lid);
		
		lid = new LanguageID(LANG_NF, LANG_NF_STR, LANG_NF_ABR);
		langIDs.put(LANG_NF, lid);
		langIDs_Desc.put(LANG_NF_STR, lid);
		
		lid = new LanguageID(LANG_NGAA, LANG_NGAA_STR, LANG_NGAA_ABR);
		langIDs.put(LANG_NGAA, lid);
		langIDs_Desc.put(LANG_NGAA_STR, lid);
		
		lid = new LanguageID(LANG_NGAJ, LANG_NGAJ_STR, LANG_NGAJ_ABR);
		langIDs.put(LANG_NGAJ, lid);
		langIDs_Desc.put(LANG_NGAJ_STR, lid);
		
		lid = new LanguageID(LANG_NGAN, LANG_NGAN_STR, LANG_NGAN_ABR);
		langIDs.put(LANG_NGAN, lid);
		langIDs_Desc.put(LANG_NGAN_STR, lid);
		
		lid = new LanguageID(LANG_NGAR, LANG_NGAR_STR, LANG_NGAR_ABR);
		langIDs.put(LANG_NGAR, lid);
		langIDs_Desc.put(LANG_NGAR_STR, lid);
		
		lid = new LanguageID(LANG_NGAW, LANG_NGAW_STR, LANG_NGAW_ABR);
		langIDs.put(LANG_NGAW, lid);
		langIDs_Desc.put(LANG_NGAW_STR, lid);
		
		lid = new LanguageID(LANG_NH, LANG_NH_STR, LANG_NH_ABR);
		langIDs.put(LANG_NH, lid);
		langIDs_Desc.put(LANG_NH_STR, lid);
		
		lid = new LanguageID(LANG_NHA, LANG_NHA_STR, LANG_NHA_ABR);
		langIDs.put(LANG_NHA, lid);
		langIDs_Desc.put(LANG_NHA_STR, lid);
		
		lid = new LanguageID(LANG_NIA, LANG_NIA_STR, LANG_NIA_ABR);
		langIDs.put(LANG_NIA, lid);
		langIDs_Desc.put(LANG_NIA_STR, lid);
		
		lid = new LanguageID(LANG_NIU, LANG_NIU_STR, LANG_NIU_ABR);
		langIDs.put(LANG_NIU, lid);
		langIDs_Desc.put(LANG_NIU_STR, lid);
		
		lid = new LanguageID(LANG_NIV, LANG_NIV_STR, LANG_NIV_ABR);
		langIDs.put(LANG_NIV, lid);
		langIDs_Desc.put(LANG_NIV_STR, lid);
		
		lid = new LanguageID(LANG_NOG, LANG_NOG_STR, LANG_NOG_ABR);
		langIDs.put(LANG_NOG, lid);
		langIDs_Desc.put(LANG_NOG_STR, lid);
		
		lid = new LanguageID(LANG_NOO, LANG_NOO_STR, LANG_NOO_ABR);
		langIDs.put(LANG_NOO, lid);
		langIDs_Desc.put(LANG_NOO_STR, lid);
		
		lid = new LanguageID(LANG_NOV, LANG_NOV_STR, LANG_NOV_ABR);
		langIDs.put(LANG_NOV, lid);
		langIDs_Desc.put(LANG_NOV_STR, lid);
		
		lid = new LanguageID(LANG_NORF, LANG_NORF_STR, LANG_NORF_ABR);
		langIDs.put(LANG_NORF, lid);
		langIDs_Desc.put(LANG_NORF_STR, lid);
		
		lid = new LanguageID(LANG_NP, LANG_NP_STR, LANG_NP_ABR);
		langIDs.put(LANG_NP, lid);
		langIDs_Desc.put(LANG_NP_STR, lid);
		
		lid = new LanguageID(LANG_NRN, LANG_NRN_STR, LANG_NRN_ABR);
		langIDs.put(LANG_NRN, lid);
		langIDs_Desc.put(LANG_NRN_STR, lid);
		
		lid = new LanguageID(LANG_NS, LANG_NS_STR, LANG_NS_ABR);
		langIDs.put(LANG_NS, lid);
		langIDs_Desc.put(LANG_NS_STR, lid);
		
		lid = new LanguageID(LANG_NYA, LANG_NYA_STR, LANG_NYA_ABR);
		langIDs.put(LANG_NYA, lid);
		langIDs_Desc.put(LANG_NYA_STR, lid);
		
		lid = new LanguageID(LANG_NYUN, LANG_NYUN_STR, LANG_NYUN_ABR);
		langIDs.put(LANG_NYUN, lid);
		langIDs_Desc.put(LANG_NYUN_STR, lid);
		
		lid = new LanguageID(LANG_OJ, LANG_OJ_STR, LANG_OJ_ABR);
		langIDs.put(LANG_OJ, lid);
		langIDs_Desc.put(LANG_OJ_STR, lid);
		
		lid = new LanguageID(LANG_OKI, LANG_OKI_STR, LANG_OKI_ABR);
		langIDs.put(LANG_OKI, lid);
		langIDs_Desc.put(LANG_OKI_STR, lid);
		
		lid = new LanguageID(LANG_OMA, LANG_OMA_STR, LANG_OMA_ABR);
		langIDs.put(LANG_OMA, lid);
		langIDs_Desc.put(LANG_OMA_STR, lid);
		
		lid = new LanguageID(LANG_OO, LANG_OO_STR, LANG_OO_ABR);
		langIDs.put(LANG_OO, lid);
		langIDs_Desc.put(LANG_OO_STR, lid);
		
		lid = new LanguageID(LANG_OSAG, LANG_OSAG_STR, LANG_OSAG_ABR);
		langIDs.put(LANG_OSAG, lid);
		langIDs_Desc.put(LANG_OSAG_STR, lid);
		
		lid = new LanguageID(LANG_OSC, LANG_OSC_STR, LANG_OSC_ABR);
		langIDs.put(LANG_OSC, lid);
		langIDs_Desc.put(LANG_OSC_STR, lid);
		
		lid = new LanguageID(LANG_OSS, LANG_OSS_STR, LANG_OSS_ABR);
		langIDs.put(LANG_OSS, lid);
		langIDs_Desc.put(LANG_OSS_STR, lid);
		
		lid = new LanguageID(LANG_OTT, LANG_OTT_STR, LANG_OTT_ABR);
		langIDs.put(LANG_OTT, lid);
		langIDs_Desc.put(LANG_OTT_STR, lid);
		
		lid = new LanguageID(LANG_PAE, LANG_PAE_STR, LANG_PAE_ABR);
		langIDs.put(LANG_PAE, lid);
		langIDs_Desc.put(LANG_PAE_STR, lid);
		
		lid = new LanguageID(LANG_PAH, LANG_PAH_STR, LANG_PAH_ABR);
		langIDs.put(LANG_PAH, lid);
		langIDs_Desc.put(LANG_PAH_STR, lid);
		
		lid = new LanguageID(LANG_PAHO, LANG_PAHO_STR, LANG_PAHO_ABR);
		langIDs.put(LANG_PAHO, lid);
		langIDs_Desc.put(LANG_PAHO_STR, lid);
		
		lid = new LanguageID(LANG_PAI, LANG_PAI_STR, LANG_PAI_ABR);
		langIDs.put(LANG_PAI, lid);
		langIDs_Desc.put(LANG_PAI_STR, lid);
		
		lid = new LanguageID(LANG_PALA, LANG_PALA_STR, LANG_PALA_ABR);
		langIDs.put(LANG_PALA, lid);
		langIDs_Desc.put(LANG_PALA_STR, lid);

		lid = new LanguageID(LANG_PALU, LANG_PALU_STR, LANG_PALU_ABR);
		langIDs.put(LANG_PALU, lid);
		langIDs_Desc.put(LANG_PALU_STR, lid);
		
		lid = new LanguageID(LANG_PANG, LANG_PANG_STR, LANG_PANG_ABR);
		langIDs.put(LANG_PANG, lid);
		langIDs_Desc.put(LANG_PANG_STR, lid);
		
		lid = new LanguageID(LANG_PANY, LANG_PANY_STR, LANG_PANY_ABR);
		langIDs.put(LANG_PANY, lid);
		langIDs_Desc.put(LANG_PANY_STR, lid);
		
		lid = new LanguageID(LANG_PAPI, LANG_PAPI_STR, LANG_PAPI_ABR);
		langIDs.put(LANG_PAPI, lid);
		langIDs_Desc.put(LANG_PAPI_STR, lid);
		
		lid = new LanguageID(LANG_PAPU, LANG_PAPU_STR, LANG_PAPU_ABR);
		langIDs.put(LANG_PAPU, lid);
		langIDs_Desc.put(LANG_PAPU_STR, lid);
		
		lid = new LanguageID(LANG_PAR, LANG_PAR_STR, LANG_PAR_ABR);
		langIDs.put(LANG_PAR, lid);
		langIDs_Desc.put(LANG_PAR_STR, lid);
		
		lid = new LanguageID(LANG_PAU, LANG_PAU_STR, LANG_PAU_ABR);
		langIDs.put(LANG_PAU, lid);
		langIDs_Desc.put(LANG_PAU_STR, lid);
		
		lid = new LanguageID(LANG_PEN, LANG_PEN_STR, LANG_PEN_ABR);
		langIDs.put(LANG_PEN, lid);
		langIDs_Desc.put(LANG_PEN_STR, lid);
		
		lid = new LanguageID(LANG_PG, LANG_PG_STR, LANG_PG_ABR);
		langIDs.put(LANG_PG, lid);
		langIDs_Desc.put(LANG_PG_STR, lid);
		
		lid = new LanguageID(LANG_PHO, LANG_PHO_STR, LANG_PHO_ABR);
		langIDs.put(LANG_PHO, lid);
		langIDs_Desc.put(LANG_PHO_STR, lid);
		
		lid = new LanguageID(LANG_PHR, LANG_PHR_STR, LANG_PHR_ABR);
		langIDs.put(LANG_PHR, lid);
		langIDs_Desc.put(LANG_PHR_STR, lid);
		
		lid = new LanguageID(LANG_PIC, LANG_PIC_STR, LANG_PIC_ABR);
		langIDs.put(LANG_PIC, lid);
		langIDs_Desc.put(LANG_PIC_STR, lid);
		
		lid = new LanguageID(LANG_PIE, LANG_PIE_STR, LANG_PIE_ABR);
		langIDs.put(LANG_PIE, lid);
		langIDs_Desc.put(LANG_PIE_STR, lid);
		
		lid = new LanguageID(LANG_PIJ, LANG_PIJ_STR, LANG_PIJ_ABR);
		langIDs.put(LANG_PIJ, lid);
		langIDs_Desc.put(LANG_PIJ_STR, lid);
		
		lid = new LanguageID(LANG_PIN, LANG_PIN_STR, LANG_PIN_ABR);
		langIDs.put(LANG_PIN, lid);
		langIDs_Desc.put(LANG_PIN_STR, lid);
		
		lid = new LanguageID(LANG_PIP, LANG_PIP_STR, LANG_PIP_ABR);
		langIDs.put(LANG_PIP, lid);
		langIDs_Desc.put(LANG_PIP_STR, lid);
		
		lid = new LanguageID(LANG_PIR, LANG_PIR_STR, LANG_PIR_ABR);
		langIDs.put(LANG_PIR, lid);
		langIDs_Desc.put(LANG_PIR_STR, lid);
		
		lid = new LanguageID(LANG_PISA, LANG_PISA_STR, LANG_PISA_ABR);
		langIDs.put(LANG_PISA, lid);
		langIDs_Desc.put(LANG_PISA_STR, lid);
		
		lid = new LanguageID(LANG_PIT, LANG_PIT_STR, LANG_PIT_ABR);
		langIDs.put(LANG_PIT, lid);
		langIDs_Desc.put(LANG_PIT_STR, lid);
		
		lid = new LanguageID(LANG_POC, LANG_POC_STR, LANG_POC_ABR);
		langIDs.put(LANG_POC, lid);
		langIDs_Desc.put(LANG_POC_STR, lid);
		
		lid = new LanguageID(LANG_POH, LANG_POH_STR, LANG_POH_ABR);
		langIDs.put(LANG_POH, lid);
		langIDs_Desc.put(LANG_POH_STR, lid);
		
		lid = new LanguageID(LANG_POLB, LANG_POLB_STR, LANG_POLB_ABR);
		langIDs.put(LANG_POLB, lid);
		langIDs_Desc.put(LANG_POLB_STR, lid);
		
		lid = new LanguageID(LANG_POLR, LANG_POLR_STR, LANG_POLR_ABR);
		langIDs.put(LANG_POLR, lid);
		langIDs_Desc.put(LANG_POLR_STR, lid);
		
		lid = new LanguageID(LANG_POR, LANG_POR_STR, LANG_POR_ABR);
		langIDs.put(LANG_POR, lid);
		langIDs_Desc.put(LANG_POR_STR, lid);
		
		lid = new LanguageID(LANG_POTA, LANG_POTA_STR, LANG_POTA_ABR);
		langIDs.put(LANG_POTA, lid);
		langIDs_Desc.put(LANG_POTA_STR, lid);
		
		lid = new LanguageID(LANG_PUL, LANG_PUL_STR, LANG_PUL_ABR);
		langIDs.put(LANG_PUL, lid);
		langIDs_Desc.put(LANG_PUL_STR, lid);
		
		lid = new LanguageID(LANG_PUM, LANG_PUM_STR, LANG_PUM_ABR);
		langIDs.put(LANG_PUM, lid);
		langIDs_Desc.put(LANG_PUM_STR, lid);
		
		lid = new LanguageID(LANG_PUNI, LANG_PUNI_STR, LANG_PUNI_ABR);
		langIDs.put(LANG_PUNI, lid);
		langIDs_Desc.put(LANG_PUNI_STR, lid);
		
		lid = new LanguageID(LANG_PUNJ, LANG_PUNJ_STR, LANG_PUNJ_ABR);
		langIDs.put(LANG_PUNJ, lid);
		langIDs_Desc.put(LANG_PUNJ_STR, lid);
		
		lid = new LanguageID(LANG_PUY, LANG_PUY_STR, LANG_PUY_ABR);
		langIDs.put(LANG_PUY, lid);
		langIDs_Desc.put(LANG_PUY_STR, lid);
		
		lid = new LanguageID(LANG_RAD, LANG_RAD_STR, LANG_RAD_ABR);
		langIDs.put(LANG_RAD, lid);
		langIDs_Desc.put(LANG_RAD_STR, lid);
		
		lid = new LanguageID(LANG_RAJ, LANG_RAJ_STR, LANG_RAJ_ABR);
		langIDs.put(LANG_RAJ, lid);
		langIDs_Desc.put(LANG_RAJ_STR, lid);
		
		lid = new LanguageID(LANG_RAP, LANG_RAP_STR, LANG_RAP_ABR);
		langIDs.put(LANG_RAP, lid);
		langIDs_Desc.put(LANG_RAP_STR, lid);
		
		lid = new LanguageID(LANG_RAR, LANG_RAR_STR, LANG_RAR_ABR);
		langIDs.put(LANG_RAR, lid);
		langIDs_Desc.put(LANG_RAR_STR, lid);
		
		lid = new LanguageID(LANG_REJ, LANG_REJ_STR, LANG_REJ_ABR);
		langIDs.put(LANG_REJ, lid);
		langIDs_Desc.put(LANG_REJ_STR, lid);
		
		lid = new LanguageID(LANG_RHY, LANG_RHY_STR, LANG_RHY_ABR);
		langIDs.put(LANG_RHY, lid);
		langIDs_Desc.put(LANG_RHY_STR, lid);
		
		lid = new LanguageID(LANG_RMA, LANG_RMA_STR, LANG_RMA_ABR);
		langIDs.put(LANG_RMA, lid);
		langIDs_Desc.put(LANG_RMA_STR, lid);
		
		lid = new LanguageID(LANG_ROH, LANG_ROH_STR, LANG_ROH_ABR);
		langIDs.put(LANG_ROH, lid);
		langIDs_Desc.put(LANG_ROH_STR, lid);
		
		lid = new LanguageID(LANG_ROM, LANG_ROM_STR, LANG_ROM_ABR);
		langIDs.put(LANG_ROM, lid);
		langIDs_Desc.put(LANG_ROM_STR, lid);
		
		lid = new LanguageID(LANG_ROTO, LANG_ROTO_STR, LANG_ROTO_ABR);
		langIDs.put(LANG_ROTO, lid);
		langIDs_Desc.put(LANG_ROTO_STR, lid);
		
		lid = new LanguageID(LANG_ROTU, LANG_ROTU_STR, LANG_ROTU_ABR);
		langIDs.put(LANG_ROTU, lid);
		langIDs_Desc.put(LANG_ROTU_STR, lid);
		
		lid = new LanguageID(LANG_RUNO, LANG_RUNO_STR, LANG_RUNO_ABR);
		langIDs.put(LANG_RUNO, lid);
		langIDs_Desc.put(LANG_RUNO_STR, lid);
		
		lid = new LanguageID(LANG_RUBY, LANG_RUBY_STR, LANG_RUBY_ABR);
		langIDs.put(LANG_RUBY, lid);
		langIDs_Desc.put(LANG_RUBY_STR, lid);
		
		lid = new LanguageID(LANG_RUSY, LANG_RUSY_STR, LANG_RUSY_ABR);
		langIDs.put(LANG_RUSY, lid);
		langIDs_Desc.put(LANG_RUSY_STR, lid);
		
		lid = new LanguageID(LANG_RUTU, LANG_RUTU_STR, LANG_RUTU_ABR);
		langIDs.put(LANG_RUTU, lid);
		langIDs_Desc.put(LANG_RUTU_STR, lid);
		
		lid = new LanguageID(LANG_SAA, LANG_SAA_STR, LANG_SAA_ABR);
		langIDs.put(LANG_SAA, lid);
		langIDs_Desc.put(LANG_SAA_STR, lid);
		
		lid = new LanguageID(LANG_SAF, LANG_SAF_STR, LANG_SAF_ABR);
		langIDs.put(LANG_SAF, lid);
		langIDs_Desc.put(LANG_SAF_STR, lid);
		
		lid = new LanguageID(LANG_SAM, LANG_SAM_STR, LANG_SAM_ABR);
		langIDs.put(LANG_SAM, lid);
		langIDs_Desc.put(LANG_SAM_STR, lid);
		
		lid = new LanguageID(LANG_SAN, LANG_SAN_STR, LANG_SAN_ABR);
		langIDs.put(LANG_SAN, lid);
		langIDs_Desc.put(LANG_SAN_STR, lid);
		
		lid = new LanguageID(LANG_SAP, LANG_SAP_STR, LANG_SAP_ABR);
		langIDs.put(LANG_SAP, lid);
		langIDs_Desc.put(LANG_SAP_STR, lid);
		
		lid = new LanguageID(LANG_SAR, LANG_SAR_STR, LANG_SAR_ABR);
		langIDs.put(LANG_SAR, lid);
		langIDs_Desc.put(LANG_SAR_STR, lid);
		
		lid = new LanguageID(LANG_SASA, LANG_SASA_STR, LANG_SASA_ABR);
		langIDs.put(LANG_SASA, lid);
		langIDs_Desc.put(LANG_SASA_STR, lid);
		
		lid = new LanguageID(LANG_SASS, LANG_SASS_STR, LANG_SASS_ABR);
		langIDs.put(LANG_SASS, lid);
		langIDs_Desc.put(LANG_SASS_STR, lid);
		
		lid = new LanguageID(LANG_SAW, LANG_SAW_STR, LANG_SAW_ABR);
		langIDs.put(LANG_SAW, lid);
		langIDs_Desc.put(LANG_SAW_STR, lid);
		
		lid = new LanguageID(LANG_SAY, LANG_SAY_STR, LANG_SAY_ABR);
		langIDs.put(LANG_SAY, lid);
		langIDs_Desc.put(LANG_SAY_STR, lid);
		
		lid = new LanguageID(LANG_SCG, LANG_SCG_STR, LANG_SCG_ABR);
		langIDs.put(LANG_SCG, lid);
		langIDs_Desc.put(LANG_SCG_STR, lid);
		
		lid = new LanguageID(LANG_SCO, LANG_SCO_STR, LANG_SCO_ABR);
		langIDs.put(LANG_SCO, lid);
		langIDs_Desc.put(LANG_SCO_STR, lid);

		lid = new LanguageID(LANG_SEB, LANG_SEB_STR, LANG_SEB_ABR);
		langIDs.put(LANG_SEB, lid);
		langIDs_Desc.put(LANG_SEB_STR, lid);

		lid = new LanguageID(LANG_SEN, LANG_SEN_STR, LANG_SEN_ABR);
		langIDs.put(LANG_SEN, lid);
		langIDs_Desc.put(LANG_SEN_STR, lid);

		lid = new LanguageID(LANG_SERA, LANG_SERA_STR, LANG_SERA_ABR);
		langIDs.put(LANG_SERA, lid);
		langIDs_Desc.put(LANG_SERA_STR, lid);

		lid = new LanguageID(LANG_SERI, LANG_SERI_STR, LANG_SERI_ABR);
		langIDs.put(LANG_SERI, lid);
		langIDs_Desc.put(LANG_SERI_STR, lid);

		lid = new LanguageID(LANG_SERU, LANG_SERU_STR, LANG_SERU_ABR);
		langIDs.put(LANG_SERU, lid);
		langIDs_Desc.put(LANG_SERU_STR, lid);

		lid = new LanguageID(LANG_SHAB, LANG_SHAB_STR, LANG_SHAB_ABR);
		langIDs.put(LANG_SHAB, lid);
		langIDs_Desc.put(LANG_SHAB_STR, lid);

		lid = new LanguageID(LANG_SHAN, LANG_SHAN_STR, LANG_SHAN_ABR);
		langIDs.put(LANG_SHAN, lid);
		langIDs_Desc.put(LANG_SHAN_STR, lid);

		lid = new LanguageID(LANG_SHEL, LANG_SHEL_STR, LANG_SHEL_ABR);
		langIDs.put(LANG_SHEL, lid);
		langIDs_Desc.put(LANG_SHEL_STR, lid);

		lid = new LanguageID(LANG_SHER, LANG_SHER_STR, LANG_SHER_ABR);
		langIDs.put(LANG_SHER, lid);
		langIDs_Desc.put(LANG_SHER_STR, lid);

		lid = new LanguageID(LANG_SHET, LANG_SHET_STR, LANG_SHET_ABR);
		langIDs.put(LANG_SHET, lid);
		langIDs_Desc.put(LANG_SHET_STR, lid);

		lid = new LanguageID(LANG_SHOR, LANG_SHOR_STR, LANG_SHOR_ABR);
		langIDs.put(LANG_SHOR, lid);
		langIDs_Desc.put(LANG_SHOR_STR, lid);

		lid = new LanguageID(LANG_SHOS, LANG_SHOS_STR, LANG_SHOS_ABR);
		langIDs.put(LANG_SHOS, lid);
		langIDs_Desc.put(LANG_SHOS_STR, lid);

		lid = new LanguageID(LANG_SHU, LANG_SHU_STR, LANG_SHU_ABR);
		langIDs.put(LANG_SHU, lid);
		langIDs_Desc.put(LANG_SHU_STR, lid);

		lid = new LanguageID(LANG_SIL, LANG_SIL_STR, LANG_SIL_ABR);
		langIDs.put(LANG_SIL, lid);
		langIDs_Desc.put(LANG_SIL_STR, lid);

		lid = new LanguageID(LANG_SIY, LANG_SIY_STR, LANG_SIY_ABR);
		langIDs.put(LANG_SIY, lid);
		langIDs_Desc.put(LANG_SIY_STR, lid);

		lid = new LanguageID(LANG_SKS, LANG_SKS_STR, LANG_SKS_ABR);
		langIDs.put(LANG_SKS, lid);
		langIDs_Desc.put(LANG_SKS_STR, lid);
		
		lid = new LanguageID(LANG_SLO, LANG_SLO_STR, LANG_SLO_ABR);
		langIDs.put(LANG_SLO, lid);
		langIDs_Desc.put(LANG_SLO_STR, lid);
		
		lid = new LanguageID(LANG_SLV, LANG_SLV_STR, LANG_SLV_ABR);
		langIDs.put(LANG_SLV, lid);
		langIDs_Desc.put(LANG_SLV_STR, lid);

		lid = new LanguageID(LANG_SOQ, LANG_SOQ_STR, LANG_SOQ_ABR);
		langIDs.put(LANG_SOQ, lid);
		langIDs_Desc.put(LANG_SOQ_STR, lid);

		lid = new LanguageID(LANG_SOT, LANG_SOT_STR, LANG_SOT_ABR);
		langIDs.put(LANG_SOT, lid);
		langIDs_Desc.put(LANG_SOT_STR, lid);

		lid = new LanguageID(LANG_SRT, LANG_SRT_STR, LANG_SRT_ABR);
		langIDs.put(LANG_SRT, lid);
		langIDs_Desc.put(LANG_SRT_STR, lid);

		lid = new LanguageID(LANG_SUMB, LANG_SUMB_STR, LANG_SUMB_ABR);
		langIDs.put(LANG_SUMB, lid);
		langIDs_Desc.put(LANG_SUMB_STR, lid);

		lid = new LanguageID(LANG_SUR, LANG_SUR_STR, LANG_SUR_ABR);
		langIDs.put(LANG_SUR, lid);
		langIDs_Desc.put(LANG_SUR_STR, lid);

		lid = new LanguageID(LANG_SVA, LANG_SVA_STR, LANG_SVA_ABR);
		langIDs.put(LANG_SVA, lid);
		langIDs_Desc.put(LANG_SVA_STR, lid);
		
		lid = new LanguageID(LANG_SWA, LANG_SWA_STR, LANG_SWA_ABR);
		langIDs.put(LANG_SWA, lid);
		langIDs_Desc.put(LANG_SWA_STR, lid);
		
		lid = new LanguageID(LANG_SYD, LANG_SYD_STR, LANG_SYD_ABR);
		langIDs.put(LANG_SYD, lid);
		langIDs_Desc.put(LANG_SYD_STR, lid);
		
		lid = new LanguageID(LANG_SYR, LANG_SYR_STR, LANG_SYR_ABR);
		langIDs.put(LANG_SYR, lid);
		langIDs_Desc.put(LANG_SYR_STR, lid);
		
		lid = new LanguageID(LANG_TAH, LANG_TAH_STR, LANG_TAH_ABR);
		langIDs.put(LANG_TAH, lid);
		langIDs_Desc.put(LANG_TAH_STR, lid);
		
		lid = new LanguageID(LANG_TAB, LANG_TAB_STR, LANG_TAB_ABR);
		langIDs.put(LANG_TAB, lid);
		langIDs_Desc.put(LANG_TAB_STR, lid);
		
		lid = new LanguageID(LANG_TACA, LANG_TACA_STR, LANG_TACA_ABR);
		langIDs.put(LANG_TACA, lid);
		langIDs_Desc.put(LANG_TACA_STR, lid);
		
		lid = new LanguageID(LANG_TACH, LANG_TACH_STR, LANG_TACH_ABR);
		langIDs.put(LANG_TACH, lid);
		langIDs_Desc.put(LANG_TACH_STR, lid);
		
		lid = new LanguageID(LANG_TAD, LANG_TAD_STR, LANG_TAD_ABR);
		langIDs.put(LANG_TAD, lid);
		langIDs_Desc.put(LANG_TAD_STR, lid);
		
		lid = new LanguageID(LANG_TAH, LANG_TAH_STR, LANG_TAH_ABR);
		langIDs.put(LANG_TAH, lid);
		langIDs_Desc.put(LANG_TAH_STR, lid);
		
		lid = new LanguageID(LANG_TAMA, LANG_TAMA_STR, LANG_TAMA_ABR);
		langIDs.put(LANG_TAMA, lid);
		langIDs_Desc.put(LANG_TAMA_STR, lid);
		
		lid = new LanguageID(LANG_TAMB, LANG_TAMB_STR, LANG_TAMB_ABR);
		langIDs.put(LANG_TAMB, lid);
		langIDs_Desc.put(LANG_TAMB_STR, lid);
		
		lid = new LanguageID(LANG_TAOS, LANG_TAOS_STR, LANG_TAOS_ABR);
		langIDs.put(LANG_TAOS, lid);
		langIDs_Desc.put(LANG_TAOS_STR, lid);
		
		lid = new LanguageID(LANG_TAR, LANG_TAR_STR, LANG_TAR_ABR);
		langIDs.put(LANG_TAR, lid);
		langIDs_Desc.put(LANG_TAR_STR, lid);
		
		lid = new LanguageID(LANG_TAS, LANG_TAS_STR, LANG_TAS_ABR);
		langIDs.put(LANG_TAS, lid);
		langIDs_Desc.put(LANG_TAS_STR, lid);
		
		lid = new LanguageID(LANG_TAU, LANG_TAU_STR, LANG_TAU_ABR);
		langIDs.put(LANG_TAU, lid);
		langIDs_Desc.put(LANG_TAU_STR, lid);
		
		lid = new LanguageID(LANG_TESA, LANG_TESA_STR, LANG_TESA_ABR);
		langIDs.put(LANG_TESA, lid);
		langIDs_Desc.put(LANG_TESA_STR, lid);

		lid = new LanguageID(LANG_TET, LANG_TET_STR, LANG_TET_ABR);
		langIDs.put(LANG_TET, lid);
		langIDs_Desc.put(LANG_TET_STR, lid);
		
		lid = new LanguageID(LANG_TIG, LANG_TIG_STR, LANG_TIG_ABR);
		langIDs.put(LANG_TIG, lid);
		langIDs_Desc.put(LANG_TIG_STR, lid);
		
		lid = new LanguageID(LANG_TIW, LANG_TIW_STR, LANG_TIW_ABR);
		langIDs.put(LANG_TIW, lid);
		langIDs_Desc.put(LANG_TIW_STR, lid);
		
		lid = new LanguageID(LANG_TOA, LANG_TOA_STR, LANG_TOA_ABR);
		langIDs.put(LANG_TOA, lid);
		langIDs_Desc.put(LANG_TOA_STR, lid);
		
		lid = new LanguageID(LANG_TOB, LANG_TOB_STR, LANG_TOB_ABR);
		langIDs.put(LANG_TOB, lid);
		langIDs_Desc.put(LANG_TOB_STR, lid);
		
		lid = new LanguageID(LANG_TON, LANG_TON_STR, LANG_TON_ABR);
		langIDs.put(LANG_TON, lid);
		langIDs_Desc.put(LANG_TON_STR, lid);
		
		lid = new LanguageID(LANG_TPR, LANG_TPR_STR, LANG_TPR_ABR);
		langIDs.put(LANG_TPR, lid);
		langIDs_Desc.put(LANG_TPR_STR, lid);
		
		lid = new LanguageID(LANG_TRA, LANG_TRA_STR, LANG_TRA_ABR);
		langIDs.put(LANG_TRA, lid);
		langIDs_Desc.put(LANG_TRA_STR, lid);
		
		lid = new LanguageID(LANG_TSA, LANG_TSA_STR, LANG_TSA_ABR);
		langIDs.put(LANG_TSA, lid);
		langIDs_Desc.put(LANG_TSA_STR, lid);
		
		lid = new LanguageID(LANG_TSC, LANG_TSC_STR, LANG_TSC_ABR);
		langIDs.put(LANG_TSC, lid);
		langIDs_Desc.put(LANG_TSC_STR, lid);
		
		lid = new LanguageID(LANG_TSH, LANG_TSH_STR, LANG_TSH_ABR);
		langIDs.put(LANG_TSH, lid);
		langIDs_Desc.put(LANG_TSH_STR, lid);
		
		lid = new LanguageID(LANG_TSW, LANG_TSW_STR, LANG_TSW_ABR);
		langIDs.put(LANG_TSW, lid);
		langIDs_Desc.put(LANG_TSW_STR, lid);
		
		lid = new LanguageID(LANG_TUAM, LANG_TUAM_STR, LANG_TUAM_ABR);
		langIDs.put(LANG_TUAM, lid);
		langIDs_Desc.put(LANG_TUAM_STR, lid);
		
		lid = new LanguageID(LANG_TUAR, LANG_TUAR_STR, LANG_TUAR_ABR);
		langIDs.put(LANG_TUAR, lid);
		langIDs_Desc.put(LANG_TUAR_STR, lid);
		
		lid = new LanguageID(LANG_TUL, LANG_TUL_STR, LANG_TUL_ABR);
		langIDs.put(LANG_TUL, lid);
		langIDs_Desc.put(LANG_TUL_STR, lid);
		
		lid = new LanguageID(LANG_TUM, LANG_TUM_STR, LANG_TUM_ABR);
		langIDs.put(LANG_TUM, lid);
		langIDs_Desc.put(LANG_TUM_STR, lid);
		
		lid = new LanguageID(LANG_TUP, LANG_TUP_STR, LANG_TUP_ABR);
		langIDs.put(LANG_TUP, lid);
		langIDs_Desc.put(LANG_TUP_STR, lid);
		
		lid = new LanguageID(LANG_TUVL, LANG_TUVL_STR, LANG_TUVL_ABR);
		langIDs.put(LANG_TUVL, lid);
		langIDs_Desc.put(LANG_TUVL_STR, lid);
		
		lid = new LanguageID(LANG_TUVN, LANG_TUVN_STR, LANG_TUVN_ABR);
		langIDs.put(LANG_TUVN, lid);
		langIDs_Desc.put(LANG_TUVN_STR, lid);
		
		lid = new LanguageID(LANG_TWE, LANG_TWE_STR, LANG_TWE_ABR);
		langIDs.put(LANG_TWE, lid);
		langIDs_Desc.put(LANG_TWE_STR, lid);
		
		lid = new LanguageID(LANG_TZ, LANG_TZ_STR, LANG_TZ_ABR);
		langIDs.put(LANG_TZ, lid);
		langIDs_Desc.put(LANG_TZ_STR, lid);
		
		lid = new LanguageID(LANG_UBY, LANG_UBY_STR, LANG_UBY_ABR);
		langIDs.put(LANG_UBY, lid);
		langIDs_Desc.put(LANG_UBY_STR, lid);
		
		lid = new LanguageID(LANG_UDI, LANG_UDI_STR, LANG_UDI_ABR);
		langIDs.put(LANG_UDI, lid);
		langIDs_Desc.put(LANG_UDI_STR, lid);
		
		lid = new LanguageID(LANG_UDM, LANG_UDM_STR, LANG_UDM_ABR);
		langIDs.put(LANG_UDM, lid);
		langIDs_Desc.put(LANG_UDM_STR, lid);
		
		lid = new LanguageID(LANG_UGA, LANG_UGA_STR, LANG_UGA_ABR);
		langIDs.put(LANG_UGA, lid);
		langIDs_Desc.put(LANG_UGA_STR, lid);
		
		lid = new LanguageID(LANG_UM, LANG_UM_STR, LANG_UM_ABR);
		langIDs.put(LANG_UM, lid);
		langIDs_Desc.put(LANG_UM_STR, lid);
		
		lid = new LanguageID(LANG_UMBR, LANG_UMBR_STR, LANG_UMBR_ABR);
		langIDs.put(LANG_UMBR, lid);
		langIDs_Desc.put(LANG_UMBR_STR, lid);
		
		lid = new LanguageID(LANG_UMBU, LANG_UMBU_STR, LANG_UMBU_ABR);
		langIDs.put(LANG_UMBU, lid);
		langIDs_Desc.put(LANG_UMBU_STR, lid);
		
		lid = new LanguageID(LANG_UMSA, LANG_UMSA_STR, LANG_UMSA_ABR);
		langIDs.put(LANG_UMSA, lid);
		langIDs_Desc.put(LANG_UMSA_STR, lid);
		
		lid = new LanguageID(LANG_UNA, LANG_UNA_STR, LANG_UNA_ABR);
		langIDs.put(LANG_UNA, lid);
		langIDs_Desc.put(LANG_UNA_STR, lid);
		
		lid = new LanguageID(LANG_UND, LANG_UND_STR, LANG_UND_ABR);
		langIDs.put(LANG_UND, lid);
		langIDs_Desc.put(LANG_UND_STR, lid);
		
		lid = new LanguageID(LANG_UNS, LANG_UNS_STR, LANG_UNS_ABR);
		langIDs.put(LANG_UNS, lid);
		langIDs_Desc.put(LANG_UNS_STR, lid);
		
		lid = new LanguageID(LANG_URU, LANG_URU_STR, LANG_URU_ABR);
		langIDs.put(LANG_URU, lid);
		langIDs_Desc.put(LANG_URU_STR, lid);
		
		lid = new LanguageID(LANG_VAI, LANG_VAI_STR, LANG_VAI_ABR);
		langIDs.put(LANG_VAI, lid);
		langIDs_Desc.put(LANG_VAI_STR, lid);
		
		lid = new LanguageID(LANG_VAN, LANG_VAN_STR, LANG_VAN_ABR);
		langIDs.put(LANG_VAN, lid);
		langIDs_Desc.put(LANG_VAN_STR, lid);
		
		lid = new LanguageID(LANG_VEND, LANG_VEND_STR, LANG_VEND_ABR);
		langIDs.put(LANG_VEND, lid);
		langIDs_Desc.put(LANG_VEND_STR, lid);
		
		lid = new LanguageID(LANG_VENE, LANG_VENE_STR, LANG_VENE_ABR);
		langIDs.put(LANG_VENE, lid);
		langIDs_Desc.put(LANG_VENE_STR, lid);
		
		lid = new LanguageID(LANG_VENT, LANG_VENT_STR, LANG_VENT_ABR);
		langIDs.put(LANG_VENT, lid);
		langIDs_Desc.put(LANG_VENT_STR, lid);
		
		lid = new LanguageID(LANG_VEP, LANG_VEP_STR, LANG_VEP_ABR);
		langIDs.put(LANG_VEP, lid);
		langIDs_Desc.put(LANG_VEP_STR, lid);
		
		lid = new LanguageID(LANG_VEST, LANG_VEST_STR, LANG_VEST_ABR);
		langIDs.put(LANG_VEST, lid);
		langIDs_Desc.put(LANG_VEST_STR, lid);
		
		lid = new LanguageID(LANG_VIL, LANG_VIL_STR, LANG_VIL_ABR);
		langIDs.put(LANG_VIL, lid);
		langIDs_Desc.put(LANG_VIL_STR, lid);
		
		lid = new LanguageID(LANG_VIN, LANG_VIN_STR, LANG_VIN_ABR);
		langIDs.put(LANG_VIN, lid);
		langIDs_Desc.put(LANG_VIN_STR, lid);
		
		lid = new LanguageID(LANG_VOL, LANG_VOL_STR, LANG_VOL_ABR);
		langIDs.put(LANG_VOL, lid);
		langIDs_Desc.put(LANG_VOL_STR, lid);
		
		lid = new LanguageID(LANG_VOR, LANG_VOR_STR, LANG_VOR_ABR);
		langIDs.put(LANG_VOR, lid);
		langIDs_Desc.put(LANG_VOR_STR, lid);
		
		lid = new LanguageID(LANG_VOT, LANG_VOT_STR, LANG_VOT_ABR);
		langIDs.put(LANG_VOT, lid);
		langIDs_Desc.put(LANG_VOT_STR, lid);
		
		lid = new LanguageID(LANG_WAB, LANG_WAB_STR, LANG_WAB_ABR);
		langIDs.put(LANG_WAB, lid);
		langIDs_Desc.put(LANG_WAB_STR, lid);
		
		lid = new LanguageID(LANG_WAG, LANG_WAG_STR, LANG_WAG_ABR);
		langIDs.put(LANG_WAG, lid);
		langIDs_Desc.put(LANG_WAG_STR, lid);
		
		lid = new LanguageID(LANG_WAL, LANG_WAL_STR, LANG_WAL_ABR);
		langIDs.put(LANG_WAL, lid);
		langIDs_Desc.put(LANG_WAL_STR, lid);
		
		lid = new LanguageID(LANG_WAND, LANG_WAND_STR, LANG_WAND_ABR);
		langIDs.put(LANG_WAND, lid);
		langIDs_Desc.put(LANG_WAND_STR, lid);
		
		lid = new LanguageID(LANG_WANG, LANG_WANG_STR, LANG_WANG_ABR);
		langIDs.put(LANG_WANG, lid);
		langIDs_Desc.put(LANG_WANG_STR, lid);
		
		lid = new LanguageID(LANG_WAP, LANG_WAP_STR, LANG_WAP_ABR);
		langIDs.put(LANG_WAP, lid);
		langIDs_Desc.put(LANG_WAP_STR, lid);
		
		lid = new LanguageID(LANG_WARI, LANG_WARI_STR, LANG_WARI_ABR);
		langIDs.put(LANG_WARI, lid);
		langIDs_Desc.put(LANG_WARI_STR, lid);
		
		lid = new LanguageID(LANG_WARO, LANG_WARO_STR, LANG_WARO_ABR);
		langIDs.put(LANG_WARO, lid);
		langIDs_Desc.put(LANG_WARO_STR, lid);
		
		lid = new LanguageID(LANG_WARP, LANG_WARP_STR, LANG_WARP_ABR);
		langIDs.put(LANG_WARP, lid);
		langIDs_Desc.put(LANG_WARP_STR, lid);
		
		lid = new LanguageID(LANG_WARY, LANG_WARY_STR, LANG_WARY_ABR);
		langIDs.put(LANG_WARY, lid);
		langIDs_Desc.put(LANG_WARY_STR, lid);
		
		lid = new LanguageID(LANG_WAWA, LANG_WAWA_STR, LANG_WAWA_ABR);
		langIDs.put(LANG_WAWA, lid);
		langIDs_Desc.put(LANG_WAWA_STR, lid);
		
		lid = new LanguageID(LANG_WCB, LANG_WCB_STR, LANG_WCB_ABR);
		langIDs.put(LANG_WCB, lid);
		langIDs_Desc.put(LANG_WCB_STR, lid);
		
		lid = new LanguageID(LANG_WEM, LANG_WEM_STR, LANG_WEM_ABR);
		langIDs.put(LANG_WEM, lid);
		langIDs_Desc.put(LANG_WEM_STR, lid);
		
		lid = new LanguageID(LANG_WEY, LANG_WEY_STR, LANG_WEY_ABR);
		langIDs.put(LANG_WEY, lid);
		langIDs_Desc.put(LANG_WEY_STR, lid);
		
		lid = new LanguageID(LANG_WIK, LANG_WIK_STR, LANG_WIK_ABR);
		langIDs.put(LANG_WIK, lid);
		langIDs_Desc.put(LANG_WIK_STR, lid);
		
		lid = new LanguageID(LANG_WIN, LANG_WIN_STR, LANG_WIN_ABR);
		langIDs.put(LANG_WIN, lid);
		langIDs_Desc.put(LANG_WIN_STR, lid);
		
		lid = new LanguageID(LANG_WIR, LANG_WIR_STR, LANG_WIR_ABR);
		langIDs.put(LANG_WIR, lid);
		langIDs_Desc.put(LANG_WIR_STR, lid);
		
		lid = new LanguageID(LANG_WOI, LANG_WOI_STR, LANG_WOI_ABR);
		langIDs.put(LANG_WOI, lid);
		langIDs_Desc.put(LANG_WOI_STR, lid);
		
		lid = new LanguageID(LANG_WOR, LANG_WOR_STR, LANG_WOR_ABR);
		langIDs.put(LANG_WOR, lid);
		langIDs_Desc.put(LANG_WOR_STR, lid);
		
		lid = new LanguageID(LANG_WRL, LANG_WRL_STR, LANG_WRL_ABR);
		langIDs.put(LANG_WRL, lid);
		langIDs_Desc.put(LANG_WRL_STR, lid);
		
		lid = new LanguageID(LANG_WU, LANG_WU_STR, LANG_WU_ABR);
		langIDs.put(LANG_WU, lid);
		langIDs_Desc.put(LANG_WU_STR, lid);
		
		lid = new LanguageID(LANG_WWU, LANG_WWU_STR, LANG_WWU_ABR);
		langIDs.put(LANG_WWU, lid);
		langIDs_Desc.put(LANG_WWU_STR, lid);
		
		lid = new LanguageID(LANG_XA, LANG_XA_STR, LANG_XA_ABR);
		langIDs.put(LANG_XA, lid);
		langIDs_Desc.put(LANG_XA_STR, lid);
		
		// alt. for XH == Xhosan
		lid = new LanguageID(LANG_XHO, LANG_XHO_STR, LANG_XHO_ABR);
		langIDs.put(LANG_XHO, lid);
		langIDs_Desc.put(LANG_XHO_STR, lid);
		
		lid = new LanguageID(LANG_XOO, LANG_XOO_STR, LANG_XOO_ABR);
		langIDs.put(LANG_XOO, lid);
		langIDs_Desc.put(LANG_XOO_STR, lid);
		
		lid = new LanguageID(LANG_YAE, LANG_YAE_STR, LANG_YAE_ABR);
		langIDs.put(LANG_YAE, lid);
		langIDs_Desc.put(LANG_YAE_STR, lid);
		
		lid = new LanguageID(LANG_YAG, LANG_YAG_STR, LANG_YAG_ABR);
		langIDs.put(LANG_YAG, lid);
		langIDs_Desc.put(LANG_YAG_STR, lid);
		
		lid = new LanguageID(LANG_YAK, LANG_YAK_STR, LANG_YAK_ABR);
		langIDs.put(LANG_YAK, lid);
		langIDs_Desc.put(LANG_YAK_STR, lid);
		
		lid = new LanguageID(LANG_YAM, LANG_YAM_STR, LANG_YAM_ABR);
		langIDs.put(LANG_YAM, lid);
		langIDs_Desc.put(LANG_YAM_STR, lid);
		
		lid = new LanguageID(LANG_YANK, LANG_YANK_STR, LANG_YANK_ABR);
		langIDs.put(LANG_YANK, lid);
		langIDs_Desc.put(LANG_YANK_STR, lid);
		
		lid = new LanguageID(LANG_YANY, LANG_YANY_STR, LANG_YANY_ABR);
		langIDs.put(LANG_YANY, lid);
		langIDs_Desc.put(LANG_YANY_STR, lid);
		
		lid = new LanguageID(LANG_YAP, LANG_YAP_STR, LANG_YAP_ABR);
		langIDs.put(LANG_YAP, lid);
		langIDs_Desc.put(LANG_YAP_STR, lid);
		
		lid = new LanguageID(LANG_YID, LANG_YID_STR, LANG_YID_ABR);
		langIDs.put(LANG_YID, lid);
		langIDs_Desc.put(LANG_YID_STR, lid);
		
		lid = new LanguageID(LANG_YIN, LANG_YIN_STR, LANG_YIN_ABR);
		langIDs.put(LANG_YIN, lid);
		langIDs_Desc.put(LANG_YIN_STR, lid);
		
		lid = new LanguageID(LANG_YOL, LANG_YOL_STR, LANG_YOL_ABR);
		langIDs.put(LANG_YOL, lid);
		langIDs_Desc.put(LANG_YOL_STR, lid);
		
		lid = new LanguageID(LANG_YOM, LANG_YOM_STR, LANG_YOM_ABR);
		langIDs.put(LANG_YOM, lid);
		langIDs_Desc.put(LANG_YOM_STR, lid);
		
		lid = new LanguageID(LANG_YON, LANG_YON_STR, LANG_YON_ABR);
		langIDs.put(LANG_YON, lid);
		langIDs_Desc.put(LANG_YON_STR, lid);
		
		lid = new LanguageID(LANG_YOP, LANG_YOP_STR, LANG_YOP_ABR);
		langIDs.put(LANG_YOP, lid);
		langIDs_Desc.put(LANG_YOP_STR, lid);
		
		lid = new LanguageID(LANG_YUM, LANG_YUM_STR, LANG_YUM_ABR);
		langIDs.put(LANG_YUM, lid);
		langIDs_Desc.put(LANG_YUM_STR, lid);
		
		lid = new LanguageID(LANG_YUM, LANG_YUM_STR, LANG_YUM_ABR);
		langIDs.put(LANG_YUM, lid);
		langIDs_Desc.put(LANG_YUM_STR, lid);
		
		lid = new LanguageID(LANG_YUP, LANG_YUP_STR, LANG_YUP_ABR);
		langIDs.put(LANG_YUP, lid);
		langIDs_Desc.put(LANG_YUP_STR, lid);
		
		lid = new LanguageID(LANG_YUR, LANG_YUR_STR, LANG_YUR_ABR);
		langIDs.put(LANG_YUR, lid);
		langIDs_Desc.put(LANG_YUR_STR, lid);
		
		lid = new LanguageID(LANG_YZA, LANG_YZA_STR, LANG_YZA_ABR);
		langIDs.put(LANG_YZA, lid);
		langIDs_Desc.put(LANG_YZA_STR, lid);
		
		lid = new LanguageID(LANG_ZE, LANG_ZE_STR, LANG_ZE_ABR);
		langIDs.put(LANG_ZE, lid);
		langIDs_Desc.put(LANG_ZE_STR, lid);
		
		lid = new LanguageID(LANG_ZI, LANG_ZI_STR, LANG_ZI_ABR);
		langIDs.put(LANG_ZI, lid);
		langIDs_Desc.put(LANG_ZI_STR, lid);
		
		lid = new LanguageID(LANG_ZOZ, LANG_ZOZ_STR, LANG_ZOZ_ABR);
		langIDs.put(LANG_ZOZ, lid);
		langIDs_Desc.put(LANG_ZOZ_STR, lid);
		
		lid = new LanguageID(LANG_ZUG, LANG_ZUG_STR, LANG_ZUG_ABR);
		langIDs.put(LANG_ZUG, lid);
		langIDs_Desc.put(LANG_ZUG_STR, lid);
		
		lid = new LanguageID(LANG_ZUNI, LANG_ZUNI_STR, LANG_ZUNI_ABR);
		langIDs.put(LANG_ZUNI, lid);
		langIDs_Desc.put(LANG_ZUNI_STR, lid);		
	}
 }
