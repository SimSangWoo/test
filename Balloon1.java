import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Balloon1 {
	
	// 풍선의 최대 레이저 적중각, 최소 레이저 적중각 저장.
	// 각도는 x축 기준. (y > 0)
	static PriorityQueue<Double[]> angles = new PriorityQueue<>(new Comparator<Double[]>() {
		@Override
		public int compare(Double[] o1, Double[] o2) {
			if(o1[1] - o2[1] != 0)
				return (int)(o1[1] - o2[1]);
			else
				return (int)(o1[0] - o2[0]);
		}
	});
	static double[] lz = new double[2];
	static int cnt;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("input.txt"));
		BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");
		
		// 레이저 발사 좌표 : (lz[0], lz[1])
		lz[0] = Double.parseDouble(st.nextToken());
		lz[1] = Double.parseDouble(st.nextToken());

		int i = 0;
		String input;

		while((input = br.readLine()) != null) {
			st = new StringTokenizer(input, " ");

			// 계산의 편의를 위해 레이저 발사 좌표를 기준으로 풍선의 좌표 설정.
			double x = Double.parseDouble(st.nextToken()) - lz[0];
			double y = Double.parseDouble(st.nextToken()) - lz[1];
			double r = Double.parseDouble(st.nextToken());
			double dSquare = x * x + y * y; 
			
			// 풍선의 최대 레이저 적중각 - 최소 레이저 적중각.
			double theta = Math.asin(2 * r * Math.sqrt(dSquare - r * r) / dSquare);
			// 풍선 중점 각도.
			double centralAngle = Math.asin(y / Math.sqrt(dSquare));
			
			if(x == 0) {
				if(y > 0)
					centralAngle = Math.PI / 2;
				if(y < 0)
					centralAngle = 3 * Math.PI / 2;
			}
			if(y == 0) {
				if(x > 0)
					centralAngle = 0;
				if(x < 0)
					centralAngle = Math.PI;
			}
			if(x < 0)
				centralAngle = Math.PI - centralAngle;

			if(x > 0)
				if(y < 0)
					centralAngle = 2 * Math.PI + centralAngle;

			double maxAngle = (centralAngle + theta / 2) * 180 / Math.PI;
			double minAngle = (centralAngle - theta / 2) * 180 / Math.PI;
			
			if(maxAngle < minAngle)
				minAngle = 180 - minAngle;

			angles.add(new Double[] {maxAngle, minAngle});
			i++;
		}

		br.close();
		
		// 레이저 각도를 풍선의 최소 적중각부터 시작해 점점 증가시킴.
		// 그러다가 터지는 풍선의 수가 줄어들기 직전에 누적된 풍선들을 모두 터트림. (Priority Queue에서 제거)
		// Priority Queue가 빌 때까지 반복.
		cnt = i;
		while(angles.size() > 1) {
			Double[] start = angles.poll();
			Double[] nextStart = angles.peek();

			// 풍선이 겹치지 않음 -> 레이저 한 번 쏴서 풍선 하나만 터트릴 수 있음.
			if(nextStart[1] > start[0])
				continue;

			double limit = Math.min(start[0], nextStart[0]);
			angles.poll();
			cnt--;
			
			while(angles.size() > 1) {
				//풍선이 겹치지 않음 -> 터지는 풍선의 수가 줄어들기 시작.
				if(limit < angles.peek()[1])
					break;
				cnt--;
				limit = Math.min(limit, angles.poll()[0]);
			}
		}

		bw.write(String.valueOf(cnt));
		bw.close();	
	}
}
