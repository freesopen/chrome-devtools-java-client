import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Tes {
    public static void main(String[] args) {
        final CompletableFuture<Integer> port = new CompletableFuture<>();
        System.out.println(port.isDone());
        port.complete(2);
        System.out.println(port.isDone());
        AtomicInteger atomicInteger = new AtomicInteger(1);
        atomicInteger.get();
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return null;
            }
        };
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,
                20, 50, TimeUnit.SECONDS,new ArrayBlockingQueue<>(100));
        Future<String> submit = threadPoolExecutor.submit(callable);
     }
}
