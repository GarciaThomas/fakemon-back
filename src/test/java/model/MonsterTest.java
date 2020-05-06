package model;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import config.fakemonConfig;
import dao.IDAOMonster;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = fakemonConfig.class)
public class MonsterTest {
	@Autowired(required = false)
	Monster monster;
	
	@Autowired(required = false)
	IDAOMonster iDAOMonster;
	

	
	//	Test de création unitaire de chaque monstre
	@Test
	public void Creation2Bebesalt() {
		assertNotNull(iDAOMonster.findById(1));
	}
	@Test
	public void Creation2Crameleon() {
		assertNotNull(iDAOMonster.findById(2));
	}
	@Test
	public void Creation2Foufoudre() {
		assertNotNull(iDAOMonster.findById(3));
	}
	@Test
	public void Creation2Pipeau() {
		assertNotNull(iDAOMonster.findById(4));
	}	
	@Test
	public void Creation2Thymtamarre() {
		assertNotNull(iDAOMonster.findById(5));
	}
	@Test
	public void Creation2Renargile() {
		assertNotNull(iDAOMonster.findById(1));
	}
	
	
	//	Tests de la fonction levelUp
	@Test
	public void levelUpTest() {
		Monster m = Player.getInstance().tableRencontre(1).get(0);
		m.levelUp();
	}
}
