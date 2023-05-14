package test;


import robocode.*;
import org.jpl7.Query;
import org.jpl7.Term;
import java.util.Arrays;
import java.util.Random;
import robocode.DeathEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

import java.awt.*;

import org.jpl7.*;


public class PrologSharedAG extends AdvancedRobot 
{
	private double potencia = 0;
    public int melhorIndice = 0;
	private static final int tam_populacao = 4;
    private Cromossomo[] populacao = new Cromossomo[tam_populacao];
    private double[] fitnessPontuacao = new double[tam_populacao];
	static boolean init = false;
	JRef r ; 
		
	
	
	private void executeQuery(String event, Term ... args)
	{
		try
		{
			int argNumber = args.length;
			
			Term[] finalArgs = new Term[1 + argNumber];
			
			finalArgs[0] = r;
			
			for(int i = 1; i <= argNumber; i++)
				finalArgs[i] = args[i - 1];	
			
			new Query(event, finalArgs).hasSolution();
		}	

//		catch (jpl.PrologException jplEx) 
	//	{ 
			//These are mostly exceptions used by the game engine 
	 	//}

		catch (Exception genEx)
		{
			genEx.printStackTrace();
		} 		
	}


	private void initializeRobot()
	{
		r = new JRef(this);
			
		if (init == false)
		{
			new Query("consult", new Atom("robots\\test\\UtilitiesShared.pl") ).hasSolution();
			new Query("consult", new Atom("robots\\test\\PrologRobotShared.pl") ).hasSolution();
								
			init = true;
		}	
	}
	
	private void personalizeRobot()
	{
		setBodyColor(Color.red);
		setGunColor(Color.black);
		setRadarColor(Color.yellow);
		setBulletColor(Color.green);
		setScanColor(Color.green);
	}
	
	public void run()
	{
		initializeRobot();			
		inicializaPopulacao();
		personalizeRobot();	

		executeQuery("startStrategy");
	}

	public void onScannedRobot(ScannedRobotEvent e)
	{
		fire(potencia);
		avaliaFitness();
		selecionaPais();
		reproducaoMutacao();
		MelhorCromossomo();
		executeQuery("onScannedRobot", new JRef(e) );
	}

	public void onDeath(DeathEvent e)
	{
		executeQuery("onDeath");
	}
    
    public class Cromossomo {
    public static final int tam_gene = 4;
    public int[] genes = new int[tam_gene];

    // Cria um cromossomo aleatório
    public Cromossomo() {
        for (int i = 0; i < tam_gene; i++) {
            genes[i] = new Random().nextInt(2);
        }
		potencia = potenciaTiro();
    }

	public int potenciaTiro() {
        int power = 0;
        for (int i = 0; i < tam_gene; i++) {
            power += genes[i] * Math.pow(2, i);
        }
        return power;
    }	

	}

    // Inicializa a população com cromossomos aleatórios
    private void inicializaPopulacao() {
        for (int i = 0; i < tam_populacao; i++) {
            populacao[i] = new Cromossomo();
        }
    }
	
    // Avalia o fitness score de cada cromossomo da população
    private void avaliaFitness() {
        for (int i = 0; i < tam_populacao; i++) {
            Cromossomo cromossomo = populacao[i];
            double life = getEnergy();
			System.out.println(life);
            double power = cromossomo.potenciaTiro();
            double fitness = life;
            fitnessPontuacao[i] = fitness;
        }

	    for (int i = 1; i < tam_populacao; i++) {
	        if (fitnessPontuacao[i] > fitnessPontuacao[melhorIndice]) {
	            melhorIndice = i;}
    	}
	}

    // Seleciona dois pais aleatórios com base no seu fitness score
    private Cromossomo[] selecionaPais() {
        Cromossomo[] pais = new Cromossomo[2];
        for (int i = 0; i < 2; i++) {
            int aleatorioIndice1 = new Random().nextInt(tam_populacao);
            int aleatorioIndice2 = new Random().nextInt(tam_populacao);
            double fitness1 = fitnessPontuacao[aleatorioIndice1];
            double fitness2 = fitnessPontuacao[aleatorioIndice2];
            pais[i] = fitness1 >= fitness2 ? populacao[aleatorioIndice1] : populacao[aleatorioIndice2];
        }
        return pais;
    }

    // Realiza a reprodução e mutação para criar a nova população
    private void reproducaoMutacao() {
        Cromossomo[] novaPopulacao = new Cromossomo[tam_populacao];

        // Elitismo: adiciona o melhor cromossomo da geração atual à nova população sem alterações
        novaPopulacao[0] = MelhorCromossomo();

        // Cria novos cromossomos com base em recombinação e mutação
        for (int i = 1; i < tam_populacao; i++) {
            Cromossomo[] pais = selecionaPais();

            // Recombinação de um ponto
            int crossoverPoint = new Random().nextInt(Cromossomo.tam_gene);
            Cromossomo filho = new Cromossomo();
            for (int j = 0; j < Cromossomo.tam_gene; j++) {
                filho.genes[j] = j < crossoverPoint ? pais[0].genes[j] : pais[1].genes[j];
            }

            // Mutação
            for (int j = 0; j < Cromossomo.tam_gene; j++) {
                if (Math.random() <= 0.1) {
                    if (filho.genes[j] == 1){
						filho.genes[j] = 0;}
					else{
						filho.genes[j] = 1;}
                }
            }

            novaPopulacao[i] = filho;
        }

        populacao = novaPopulacao;
    }

	// Retorna o melhor cromossomo da população atual
	public Cromossomo MelhorCromossomo() {
	Cromossomo melhorCromossomo = populacao[melhorIndice];
   
    return melhorCromossomo;
	}
}