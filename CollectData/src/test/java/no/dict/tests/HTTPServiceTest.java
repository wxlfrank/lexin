package no.dict.tests;

public class HTTPServiceTest{

//	public Map<String, DictItem> getHash(String table) throws SQLException{
//		Map<String, DictItem> hash = new HashMap<String, DictItem>();
//		Connection c2 = null;
//		Statement stmt2 = null;
//		try {
//			c2 = SQLiteService.getConnection();
//			stmt2 = c2.createStatement();
//		}catch (ClassNotFoundException | SQLException e) {
//			System.err.println(e.getClass().getName() + ": " + e.getMessage());
//			return null;	            
//		}
//
//		String sql = "select * from " + table + "";
//
//		ResultSet rs = stmt2.executeQuery(sql);
//		while(rs.next()){
//			String word = rs.getString("word");
//			String explain = rs.getString("explain");
//			hash.put(word + "|" + explain,  null);
//		}
//		return hash;
//	}
	
	/* @Test
	public void testDocumentHasNext(){
		Document doc = HttpService.getDocument(HttpService.getURL("poeng", 0));
		Assert.assertTrue(HttpService.hasNext(doc));
		doc = HttpService.getDocument(HttpService.getURL("skylder", 0));
		Assert.assertTrue(HttpService.hasNext(doc));
	} */
	
//	public void testGetItemFromDocument(){
//		Document doc = HttpService.getDocument(HttpService.getURL("pluss", 0));
//		List<DictItem> items = HttpService.getDictItems(doc);
//		for(DictItem iter : items){
//			System.out.println(iter);
//		}
//		Assert.assertTrue(items.size() == 5);
//		doc = HttpService.getDocument(HttpService.getURL("pluss", 5));
//		items = HttpService.getDictItems(doc);
//		for(DictItem iter : items){
//			System.out.println(iter);
//		}
//		Assert.assertTrue(items.size() == 1);
//		for(int index = 0; index < 305; index = index + 5){
//			doc = HttpService.getDocument(HttpService.getURL("jeg", index));
//			items = HttpService.getDictItems(doc);
//			for(DictItem iter : items){
//				System.out.println(iter);
//			}
//			Assert.assertTrue(items.size() == 5);
//		}
//		doc = HttpService.getDocument(HttpService.getURL("jeg", 305));
//		items = HttpService.getDictItems(doc);
//		for(DictItem iter : items){
//			System.out.println(iter);
//		}
//		Assert.assertTrue(items.size() == 4);
//		for(int index = 310; index < 800; index = index + 5){
//			doc = HttpService.getDocument(HttpService.getURL("jeg", index));
//			items = HttpService.getDictItems(doc);
//			for(DictItem iter : items){
//				System.out.println(iter);
//			}
//			Assert.assertTrue(items.size() == 5);
//		}
//		doc = HttpService.getDocument(HttpService.getURL("jeg", 800));
//		items = HttpService.getDictItems(doc);
//		for(DictItem iter : items){
//			System.out.println(iter);
//		}
//		Assert.assertTrue(items.size() == 3);
//	}
//	public void updateDataBase() throws Exception{
//		Map<String, DictItem> hash = getHash("other");
//		char table = 'a';
//		while(table <= 'z'){
//			hash.putAll(getHash("" + table++));
//		}
//
//		Connection c = null;
//		Statement stmt = null;
//		try {
//			c = SQLiteService.getConnection();
//			stmt = c.createStatement();
//		}catch (ClassNotFoundException | SQLException e) {
//			System.err.println(e.getClass().getName() + ": " + e.getMessage());
//			return;	            
//		} 
//		Set<String> searched = new HashSet<String>();
//		Set<String> unsearched = new HashSet<String>();
//		for(String key : hash.keySet())
//			searched.add(key.substring(0, key.indexOf('|')));
//		unsearched.add("baud");
//		unsearched.add("ser");
//		unsearched.add("rauv");
//		unsearched.add("yngst");
//		while(!unsearched.isEmpty()){
//			String word = unsearched.iterator().next();
//			int count = 0;
//			int from = 0;
//			Document document = null;
//			System.out.println("**" + word + " unsearched: " + unsearched.size() + " searched: " + searched.size());
//			do{
//				document = HttpService.getDocumentFromWeb(word, from);
//				if(document == null)
//					break;
//				List<DictItem> result = HttpService.createDictItemFromDocument(document, hash, searched, unsearched);
//				if(result == null)
//					break;
//				for(DictItem item : result){
//					System.out.println(item.getWord());
//					SQLiteService.addExplainationToDatabase(stmt, item);
//				}
//				from += 5;
//				count += result.size();
//			}while(HttpService.hasNext(document));
//			System.out.println("items for " + word + " searched:" + count);
//			System.out.println("***************************************************");
//			unsearched.remove(word);
//			searched.add(word);
//		}
//
//	}


//	public List<String> compareTable(String table) throws SQLException{
//
//		List<String> result = new ArrayList<String>();
//		Connection c1 = null, c2 = null;
//		Statement stmt1 = null, stmt2 = null;
//		try {
//			c2 = SQLiteService.getConnection("Olddict.db");
//			stmt2 = c2.createStatement();
//		}catch (ClassNotFoundException | SQLException e) {
//			System.err.println(e.getClass().getName() + ": " + e.getMessage());
//			return null;	            
//		}
//
//		String sql = "select distinct word from " + table;
//
//		ResultSet rs = stmt2.executeQuery(sql);
//		while(rs.next()){
//			String word = rs.getString("word");
//			result.add(word);
//		}
//		stmt2.close();
//		c2.close();
//		try {
//			c1 = SQLiteService.getConnection();
//			stmt1 = c1.createStatement();
//		}catch (ClassNotFoundException | SQLException e) {
//			System.err.println(e.getClass().getName() + ": " + e.getMessage());
//			return null;	            
//		}
//		rs = stmt1.executeQuery(sql);
//		while(rs.next()){
//			String word = rs.getString("word");
//			if(result.contains(word))
//				result.remove(word);
//		}
//		stmt1.close();
//		c1.close();
//		return result;
//	}

//	@Test
//	public void compareTable() throws SQLException{
//		Set<String> result = new HashSet<String>();
//		result.addAll(compareTable("other"));
//		char table = 'a';
//		while(table <= 'z'){
//			result.addAll(compareTable("" + table++));
//		}
//		for(String iter : result)
//			System.out.println(iter);
//	}
//	public List<String> printClazz(String table) throws SQLException{
//		List<String> result = new ArrayList<String>();
//		Connection c = null;
//		Statement stmt = null;
//		try {
//			c = SQLiteService.getConnection();
//			stmt = c.createStatement();
//		}catch (ClassNotFoundException | SQLException e) {
//			System.err.println(e.getClass().getName() + ": " + e.getMessage());
//			return null;	            
//		} 
//		String sql = "select distinct clazz from " + table;
//		ResultSet rs = stmt.executeQuery(sql);
//		while(rs.next()){
//			String word = rs.getString("clazz");
//			result.add(word);
//		}
//		rs.close();
//		stmt.close();
//		c.close();
//		return result;
//	}
//	@Test
//	public void testClazz() throws SQLException{
//		Set<String> result = new HashSet<String>();
//		result.addAll(printClazz("other"));
//		char table = 'a';
//		while(table <= 'z'){
//			result.addAll(printClazz("" + table++));
//		}
//		for(String iter : result)
//			System.out.println(iter);
//	}
//	@Test
//	public void testDatabase() throws Exception {
//		Set<String> unsearched = new HashSet<String>();
//
//		BufferedReader reader  = new BufferedReader(new FileReader("words.txt"));
//		String line  = reader.readLine();
//		while((line = reader.readLine()) != null){
//			unsearched.add(line.split(" ")[0]);
//		}
//		reader.close();
//
//		Connection c = null;
//		Statement stmt = null;
//		try {
//			c = SQLiteService.getConnection();
//			stmt = c.createStatement();
//		}catch (ClassNotFoundException | SQLException e) {
//			System.err.println(e.getClass().getName() + ": " + e.getMessage());
//			return;	            
//		} 
//		//	        finally {
//		//	            try {
//		//	                if (c != null) {
//		//	                    c.close();
//		//	                }
//		//	            } catch (SQLException ex) {
//		//	                Logger.getLogger(SQLiteService.class.getName()).log(Level.SEVERE, null, ex);
//		//	            }
//		//	        }
////		while(!unsearched.isEmpty()){
////			String word = unsearched.iterator().next();
////			int count = 0;
////			int from = 0;
////			Document document = null;
////			System.out.println("**" + word + " unsearched: " + unsearched.size() + " searched: " + searched.size());
////			do{
////				document = HttpService.getDocumentFromWeb(word, from);
////				if(document == null)
////					break;
////				List<DictItem> result = HttpService.createDictItemFromDocument(document, hash, searched, unsearched);
////				if(result == null)
////					break;
////				for(DictItem item : result){
////					System.out.println(item.getWord());
////					SQLiteService.addExplainationToDatabase(stmt, item);
////				}
////				from += 5;
////				count += result.size();
////			}while(HttpService.hasNext(document));
////			System.out.println("items for " + word + " searched:" + count);
////			System.out.println("***************************************************");
////			unsearched.remove(word);
////			searched.add(word);
////		}
//	}

//	@Test
//	public void testWord() throws Exception{
//		Map<String, DictItem> hash = new HashMap<String, DictItem>();
//		Set<String> searched = new HashSet<String>();
//		Set<String> unsearched = new HashSet<String>();
//		Connection c = null;
//		Statement stmt = null;
//		try {
//			c = SQLiteService.getConnection();
//			stmt = c.createStatement();
//		}catch (ClassNotFoundException | SQLException e) {
//			System.err.println(e.getClass().getName() + ": " + e.getMessage());
//			return;	            
//		} 
//		int from = 0;
//		Document document = null;
//		String word = "skyldes";
//		System.out.println("********************" + word);
//		do{
//			document = HttpService.getDocumentFromWeb(word, from);
//			if(document == null)
//				break;
//			List<DictItem> result = HttpService.createDictItemFromDocument(document, hash, searched, unsearched);
//			for(DictItem item : result){
//				SQLiteService.addExplainationToDatabase(stmt, item);
//			}
//			from += 5;
//		}while(HttpService.hasNext(document));
//		unsearched.remove(word);
//		searched.add(word);
//	}

//	@Test
//	public void deleteWord() throws SQLException, IOException{
//		deleteWord("other");
//		char table = 'a';
//		while(table <= 'z'){
//			deleteWord("" + table++);
//		}
//		testDB();
//	}
//	public void deleteWord(String table) throws SQLException, IOException{
//		Connection c = null;
//		Statement stmt = null;
//		try {
//			c = SQLiteService.getConnection();
//			stmt = c.createStatement();
//		}catch (ClassNotFoundException | SQLException e) {
//			System.err.println(e.getClass().getName() + ": " + e.getMessage());
//			return;	            
//		} 
//		String	sql = "select word from " + table;
//		ResultSet rs = stmt.executeQuery(sql);
//		List<String> words = new ArrayList<String>();
//		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//		while(rs.next()){
//			String word = rs.getString("word");
//			if(!word.matches("[a-zA-ZåøæØÅÆéêàô!'\\./ \\(\\)\\-]*")){
//				System.out.println(word);
//				String read = reader.readLine();
//				if(read.trim().equals("1"))
//					words.add(word);
//			}
//		}
//		rs.close();
//		stmt.close();
//		c.close();
//		if(!words.isEmpty()){
//			try {
//				c = SQLiteService.getConnection();
//				c.setAutoCommit(false);			
//				stmt = c.createStatement();
//			}catch (ClassNotFoundException | SQLException e) {
//				System.err.println(e.getClass().getName() + ": " + e.getMessage());
//				return;	            
//			} 
//			for(String word: words){
//				sql = "delete from " + table + " where word='" + word + "'";
//				System.out.println(sql);
//				stmt.addBatch(sql);
//			}
//			stmt.executeBatch();
//			stmt.close();
//			c.commit();
//			c.close();
//		}
//	}

//	@Test 
//	public void fixClazz() throws SQLException, IOException{
//		fixClazz("other");
//		char table = 'a';
//		while(table <= 'z'){
//			fixClazz("" + table++);
//		}
//		testDB();
//	}

//	public void fixClazz(String table) throws SQLException, IOException{
//		Connection c = null;
//		Statement stmt = null;
//		try {
//			c = SQLiteService.getConnection();
//			stmt = c.createStatement();
//		}catch (ClassNotFoundException | SQLException e) {
//			System.err.println(e.getClass().getName() + ": " + e.getMessage());
//			return;	            
//		} 
//		String sql = "select word, explain, clazz, format from " + table;
//		ResultSet rs = stmt.executeQuery(sql);
//		List<String> words = new ArrayList<String>();
//		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//		while(rs.next()){
//			String word = rs.getString("word");
//			String explain = rs.getString("explain");
//			String clazz = rs.getString("clazz");
//			String format = rs.getString("format");
//			if(!clazz.matches("[a-zA-ZåøæØÅÆ ,\\(\\)]*")){
//				System.out.println(word);
//				System.out.println(explain);
//				System.out.println(clazz);
//				System.out.println(format);
//				String read = reader.readLine();
//				if(read != null && !read.equals("0")){
//					words.add(word);
//					words.add(explain);
//					if(read.equals("1"))
//						words.add("substantive");
//					else 
//						words.add(read);
//					words.add(clazz);
//				}
//			}
//		}
//		rs.close();
//		stmt.close();
//		c.close();
//
//		if(!words.isEmpty()){
//			try {
//				c = SQLiteService.getConnection();
//				c.setAutoCommit(false);			
//				stmt = c.createStatement();
//			}catch (ClassNotFoundException | SQLException e) {
//				System.err.println(e.getClass().getName() + ": " + e.getMessage());
//				return;	            
//			} 
//			for (int i = 0; i < words.size(); i= i + 4) {
//				String word = words.get(i);
//				String explain = words.get(i + 1);
//				String clazz = words.get(i + 2);
//				String format = words.get(i + 3);
//				sql = "update " + table + " set clazz='" + clazz + "', format='" + format + "' where word='" + word + "' and explain = '" + explain + "'";
//				System.out.println(sql);
//				stmt.addBatch(sql);
//			}
//			stmt.executeBatch();
//			stmt.close();
//			c.commit();
//		}
//	}

//	@Test 
//	public void testDB() throws SQLException, IOException{
//		Connection c = null;
//		Statement stmt = null;
//		try {
//			c = SQLiteService.getConnection();
//			c.setAutoCommit(false);
//			stmt = c.createStatement();
//		}catch (ClassNotFoundException | SQLException e) {
//			System.err.println(e.getClass().getName() + ": " + e.getMessage());
//			return;	            
//		} 
//		char table = 's';
//		while(table <= 'z'){
//			String sql = "select * from " + table;
//			ResultSet result = SQLiteService.query(stmt, sql);
//			while(result.next()){
//				DictItem dictItem = SQLiteService.getDictItem(result);
//				if(!dictItem.getWord().matches("[a-zA-ZåøæØÅÆéêàô!'/ \\.\\(\\)\\-]*")){
//					System.out.println("word " + dictItem);
//					break;
//				}
//				else if(!dictItem.getExplain().matches("[0-9a-zA-ZåøæØÅÆ«»é@§´%ôàá_<>=+'!?^\\\\/{}\\* \\(\\)\\[\\],~;\":.|\\-]*"))
//				{
//					System.out.println("explain " + dictItem);
//					break;
//				}
//				else if(!dictItem.getClazz().matches("[a-zA-ZåøæØÅÆ ,\\(\\)]*"))
//				{
//					System.out.println("clazz " + dictItem);
//					break;
//				}
//				else if(!dictItem.getComment().matches("[0-9a-zA-ZåøæØÅÆ;, \\-]*"))
//				{
//					System.out.println("comment " + dictItem);
//					break;
//				}
//				else if(!dictItem.getAlternative().matches("[a-zA-ZåøæØÅÆé \\(\\)\\-.|]*"))
//				{
//					System.out.println("alternative " + dictItem);
//					break;
//				}
//				else if(!dictItem.getComposite().matches("[0-9a-zA-ZåøæØÅÆ,é!ô |\"\\-\\(\\)/]*"))
//				{
//					System.out.println("composite " + dictItem);
//					break;
//				}
//				else if(!dictItem.getChange().matches("[a-zA-ZåøæØÅÆéêô',:;|\\^ \\[\\]\\(\\)\\-]*"))
//				{
//					System.out.println("foramt " + dictItem);
//					break;
//				}
//				else if(!dictItem.getError().matches("[0-9a-zA-ZåøæØÅÆ |\\-\\.\\(\\)\\[\\]\"!,:;?\r\n]*"))
//				{
//					System.out.println("error " + dictItem);
//					break;
//				}
//				else if(!dictItem.getPhrases().matches("[0-9a-zA-ZåøæØÅÆöé./ |\\(\\),\";?!\\-]*"))
//				{
//					System.out.println("phrases " + dictItem);
//					break;
//				}
//				else if(!dictItem.getExamples().matches("[0-9a-zA-ZäåøæØÅÆöÖèéà:;@%.=+' |/\\-\\.\\(\\)\"!,?]*"))
//				{
//					System.out.println("examples " + dictItem);
//					break;
//				}
//			}
//			table = (char) (table + 1);
//		}
//		c.close();
//	}



}
