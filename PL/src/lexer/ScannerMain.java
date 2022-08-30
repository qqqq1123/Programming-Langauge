//package lexer;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStreamWriter;
//import java.io.Writer;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.stream.Stream;
//
//public class ScannerMain {
//	public static final void main(String... args) throws Exception { /* throws 원본 보기 */
//		ClassLoader cloader = ScannerMain.class.getClassLoader();
//		File file = new File(cloader.getResource("lexer/as04.txt").getFile());
//		testTokenStream(file);
//
//	}
//
//	// use tokens as a Stream
//	private static void testTokenStream(File file) {
//		Stream<Token> tokens = Scanner.stream(file);
//		Path path = Paths.get("/Users/mini/Desktop/hw04.txt");
//		try (Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path.toFile())))) {
//
//			tokens.map(ScannerMain::toString).forEach(t-> {
//				try {
//					w.write(t);
//					System.out.println(t);
//					((BufferedWriter) w).newLine();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			});
//
//		} catch (IOException e) {
//		}
//
//	}
//
//	private static String toString(Token token) {
//		return String.format("%-3s: %s", token.type().name(), token.lexme());
//	}
//
//}
