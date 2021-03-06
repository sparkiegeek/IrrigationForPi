package au.org.noojee.irrigation.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.pi4j.io.gpio.Pin;

import au.org.noojee.irrigation.entities.EndPoint;
import au.org.noojee.irrigation.entities.GardenBed;

public class GardenBedDao
{

	@SuppressWarnings("unchecked")
	public List<GardenBed> getAll()
	{
		EntityManager em = EntityManagerProvider.getEntityManager();

		Query query = em.createQuery("SELECT e FROM GardenBed e");
		return (List<GardenBed>) query.getResultList();
	}

	/**
	 * Returns all garden beds which are controlled by the given master valve.
	 * 
	 * @param masterValve
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<GardenBed> getControlledBy(EndPoint masterValve)
	{
		EntityManager em = EntityManagerProvider.getEntityManager();

		Query query = em.createQuery("SELECT e FROM GardenBed e where e.masterValve = :masterValve");
		query.setParameter("masterValve", masterValve);

		return (List<GardenBed>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<GardenBed> getByValve(EndPoint valve)
	{

		EntityManager em = EntityManagerProvider.getEntityManager();
		Query query = em.createQuery("SELECT e FROM GardenBed e where e.valve = :valve");
		query.setParameter("valve", valve);

		return (List<GardenBed>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<EndPoint> getByPin(Pin piPin)
	{
		EntityManager em = EntityManagerProvider.getEntityManager();

		Query query = em
				.createQuery("SELECT e FROM EndPoint e where e.pinNo = :pinNo order by LOWER(e.endPointName)");
		query.setParameter("pinNo", piPin.getAddress());

		return (List<EndPoint>) query.getResultList();
	}

	public void deleteAll()
	{
		EntityManager em = EntityManagerProvider.getEntityManager();

		Query q2 = em.createQuery("DELETE FROM GardenBed e");
		q2.executeUpdate();
	}

	public void persist(GardenBed gardenBed)
	{
		EntityManager em = EntityManagerProvider.getEntityManager();
		em.persist(gardenBed);
	}

	public void delete(GardenBed gardenBed)
	{
		EntityManager em = EntityManagerProvider.getEntityManager();
		gardenBed = em.find(GardenBed.class, gardenBed.getId());

		em.remove(gardenBed);

	}

	public void merge(GardenBed GardenBed)
	{
		EntityManager em = EntityManagerProvider.getEntityManager();
		em.merge(GardenBed);
	}

}
