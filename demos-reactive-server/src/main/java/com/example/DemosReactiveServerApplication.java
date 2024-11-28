package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow.Subscription;
import java.util.logging.Logger;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@SpringBootApplication
public class DemosReactiveServerApplication implements CommandLineRunner {
	private static Logger logger = Logger.getLogger("Este si");

	public static void main(String[] args) {
		SpringApplication.run(DemosReactiveServerApplication.class, args);
	}

	public static class MessageService {
		List<FluxSink> sinks = new ArrayList<FluxSink>();
		public void add(FluxSink sink) {
			sinks.add(sink);
			sink.onDispose(() -> sinks.remove(sink));
		}
		public void emit(List<String> chunk) {
			chunk.forEach(item -> sinks.forEach(sink -> sink.next(item)));
		}
		public void processComplete() { List.copyOf(sinks).forEach(FluxSink::complete); }
	}

	@Override
	public void run(String... args) throws Exception {
////		Mono<String> mono = Mono.just("Adios mundo");
////		mono = Mono.error(() -> new InvalidAttributesException());
////		mono = Mono.never();
//////		mono = Mono.empty();
////		System.out.println("no he esperado");
////		Mono<Date> mono = Mono.fromSupplier(() -> new Date());
//		Mono<Date> mono = Mono.just(new Date());
//		System.out.println("Directo: " + (new Date()).toLocaleString());
//		Thread.sleep(1000);
//		mono.subscribe(item -> System.out.println(item.toLocaleString()), err -> err.printStackTrace(), () -> System.out.println(" y termine"));
////		mono.subscribe(item -> System.out.print(item), err -> err.printStackTrace(), () -> System.out.println(" y termine"));
////		Flux.just(2,4,6,8,10).subscribe(item -> System.out.println(item));
////		var x = IntStream.range(0, 5).map(item -> Integer.valueOf(item)).toArray();
////		Flux.fromIterable(List.of("uno", "dos", "tres")).subscribe(item -> System.out.println(item));
//		class Par { int fila = 0; int columna = 0; }
//		Flux<String> flux = Flux.generate(() -> new Par(), (estado, suscriptor) ->{
//			if(estado.fila == 2 && estado.columna == 2) throw new ArithmeticException("No me se el 2x2");
//			suscriptor.next(estado.fila + " x " + estado.columna + " = " + estado.fila * estado.columna);
//			if(estado.fila == 10 && estado.columna == 10) suscriptor.complete();
//			if(estado.columna == 10) {
//				estado.fila++;
//				estado.columna = 0;
//				return estado;
//			} else {
//				estado.columna++;
//				return estado;
//			}
//		});
////
////		 imprime(flux);
//		// imprime(Flux.empty());
////		imprime(Flux.error(new Exception("Sin datos")));
//		//imprime(Flux.never());
////		var srv = new MessageService();
////		Flux<String> flux = Flux.create(sink -> srv.add(sink));
////		Flux<String> otro = Flux.create(sink -> srv.add(sink));
////		imprime(flux);
////		srv.emit(List.of("uno", "dos", "tres"));
////		otro.subscribe(item -> logger.info(item));
////		//srv.processComplete();
////		srv.emit(List.of("esta", "ya", "no", "llega"));
////		Flux.just(1, 2, 0, 10)
////			.doOnNext(item -> System.out.println("Original: " + item))
////			.filter(item -> item % 2 == 0)
////			.doOnNext(item -> System.out.println("Pares: " + item))
////			.map(i -> "100 / " + i + " = " + (100 / i)) //this triggers an error with 0
////			.onErrorReturn("Divided by zero :(") // error handling example
////			.subscribe(item -> System.out.println(item), err -> err.printStackTrace());
////		var flux1 = 		Flux.just(1, 2, 5, 10)
////			.doOnNext(item -> System.out.println("Original: " + item))
////			.filter(item -> item % 2 == 0)
////			.doOnNext(item -> System.out.println("Pares: " + item))
////			.map(i -> "100 / " + i + " = " + (100 / i));
////		var flux2 = Flux.fromIterable(List.of("uno", "dos", "tres"));
////		imprime(Flux.merge(flux1, flux2));
//		
//		int page=0, rows=20;
////		imprime(Flux.range(0, 1000).skip(page*rows).take(rows).map(item -> item.toString()));
//		boolean filtro = false;
//		var flux1 = Flux.just(1, 2, 0, 10)
//				.doOnNext(item -> System.out.println("Original: " + item));
//		if(filtro)
//			flux1 = flux1.filter(item -> item % 2 == 0)
//				.doOnNext(item -> System.out.println("Pares: " + item));
//		Flux<String> flux2;
//		if(page > 0)
//			flux2 = flux1.map(i -> "100 / " + i + " = " + (100 / i)) //this triggers an error with 0
//				.onErrorReturn("Divided by zero :("); // error handling example
//		else {
//			flux2 = flux1.map(item -> item.toString());
//		}
//		// ...
//		imprime(flux2);
//
//
	}
	
	public void imprime(Flux<String> flux) {
		class SampleSubscriber<T> extends BaseSubscriber<T> {
			public void hookOnSubscribe(Subscription subscription) {
				System.out.println("Subscribed");
				request(1);
			}
			public void hookOnNext(T value) {
				System.out.println(value);
				request(1);
			}
			@Override
			protected void hookOnError(Throwable err) {
				err.printStackTrace();
				super.hookOnError(err);
			}
		}

//		var suscripcion = flux.subscribe(item -> System.out.println(item), err -> err.printStackTrace());
//		suscripcion.dispose();
//		var suscriptor = new SampleSubscriber();
//		flux.subscribe(suscriptor);
//		suscriptor.cancel();
		flux.subscribe(item -> System.out.println("otra->" + item), err -> err.printStackTrace());
		System.out.println("termine");

	}

}
