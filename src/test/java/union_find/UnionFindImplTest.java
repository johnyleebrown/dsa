package union_find;

import org.junit.jupiter.api.Test;
import reader.InputReader;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static reader.IOUtils.getFolderFilesReaders;

class UnionFindImplTest {
	@Test
	void test1() {
		List<InputReader> readers = getFolderFilesReaders(getClass());
		for (InputReader r : readers) {
			int answer = r.nextInt();
			int n = r.nextInt();
			UnionFindImpl uf = new UnionFindImpl(n);

			for (int j = 0; j < n; j++) {
				uf.union(r.nextInt(), r.nextInt());
			}

			assertEquals(answer, uf.count());
		}
	}
}