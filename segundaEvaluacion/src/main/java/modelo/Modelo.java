package modelo;

import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import clasesHibernate.Departamentos;
import clasesHibernate.Empleados;
import jakarta.persistence.TypedQuery;

public class Modelo {
	// conecta con el config del hibernate
	private static final Configuration cfg = new Configuration().configure();
	// creas la sesión
	private static final SessionFactory sf = cfg.buildSessionFactory();
	private static Session sesion;

	public static void main(String[] args) {
		// anadirEmpleado("Juan", "Perez", "García", "Enfermería");
		// anadirDepartamento("Enfermeria", "Huelva");
		// borrarDepartamento(44);
//		ArrayList<Departamentos> listadpto = listarDpto("Sevilla");
//		for (Departamentos a : listadpto) {
//			System.out.println(a.toString());
//		}
//		ArrayList<Empleados> listEmpleados = listarEmpleados("Contabilidad");
//
//		if (listEmpleados.size() == 0) {
//			System.out.println("No hay ningun empleado en este departamento");
//		} else {
//			for (Empleados e : listEmpleados) {
//				System.out.println(e.toString());
//
//			}
//		}
//		listEmpleados = listarEmpleados();
//		if (listEmpleados.size() == 0) {
//			System.out.println("No hay ningun empleado en este departamento");
//		} else {
//			for (Empleados e : listEmpleados) {
//				if (e.getDepartamentos() != null) {
//					System.out.println(e.toString() + "[" + e.getDepartamentos().getDnombre() + "]");
//
//				}
//
//			}
//		}
		modificarEmpleado("alfredo", "garcia", "maruenda", "Jardin23");

//		if (borrarEmpleado("Juaa1a23an", "Maaumel", "Jacinto")) {
//			System.out.println("Empleado eliminado");
//		} else {
//			System.out.println("El empleado no existe");
//		}

	}

	private static ArrayList<Departamentos> listarDpto() {
		String hql = "from Departamentos";
		sesion = sf.openSession();
		TypedQuery<Departamentos> tqDpto = sesion.createQuery(hql, Departamentos.class);
		ArrayList<Departamentos> arrayDpto = (ArrayList<Departamentos>) tqDpto.getResultList();
		sesion.close();
		return arrayDpto;

	}

	private static ArrayList<Departamentos> listarDpto(int dptoNum) {
		String hql = "from Departamentos where deptNo=" + dptoNum;
		sesion = sf.openSession();
		TypedQuery<Departamentos> tqDpto = sesion.createQuery(hql, Departamentos.class);
		return (ArrayList<Departamentos>) tqDpto.getResultList();

	}

	private static ArrayList<Departamentos> listarDpto(String localidad) {
		String hql = "from Departamentos where loc='" + localidad + "'";
		sesion = sf.openSession();
		TypedQuery<Departamentos> tqDpto = sesion.createQuery(hql, Departamentos.class);
		return (ArrayList<Departamentos>) tqDpto.getResultList();

	}

	private static ArrayList<Departamentos> listarDptoNombre(String nombre) {
		String hql = "from Departamentos where dnombre='" + nombre + "'";
		sesion = sf.openSession();
		TypedQuery<Departamentos> tqDpto = sesion.createQuery(hql, Departamentos.class);
		return (ArrayList<Departamentos>) tqDpto.getResultList();

	}

	private static ArrayList<Empleados> listarEmpleados() {
		String hql = "from Empleados";
		sesion = sf.openSession();
		TypedQuery<Empleados> tqEmp = sesion.createQuery(hql, Empleados.class);
		ArrayList<Empleados> listaEmpleados = (ArrayList<Empleados>) tqEmp.getResultList();
		return listaEmpleados;

	}

	private static ArrayList<Empleados> listarEmpleados(String nombreDepartamento) {
		// from Empleados where departamentos.dnombre = 'nombreDepartamento'
		String hql = "from Departamentos where dnombre='" + nombreDepartamento + "'";
		sesion = sf.openSession();
		TypedQuery<Departamentos> tqDpto = sesion.createQuery(hql, Departamentos.class);
		ArrayList<Departamentos> tqDep = (ArrayList<Departamentos>) tqDpto.getResultList();
		Departamentos departamento = tqDep.get(0);

		hql = "from Empleados where departamentos = :departamento";
		TypedQuery<Empleados> tqEmp = sesion.createQuery(hql, Empleados.class);
		tqEmp.setParameter("departamento", departamento);
		ArrayList<Empleados> arrayempleados = (ArrayList<Empleados>) tqEmp.getResultList();
		sesion.close();
		return arrayempleados;

	}

	private static void modificarDpto(int idDpto, String nombreNew, String localidadNew) {

		// String hql = "from Departamentos where loc='" + localidad + "'";
		sesion = sf.openSession();
		Transaction t = sesion.beginTransaction();

		Departamentos dpto = sesion.get(Departamentos.class, idDpto);
		if (nombreNew != null) {

			dpto.setDnombre(nombreNew);

		}
		if (localidadNew != null) {
			dpto.setLoc(localidadNew);
		}
		sesion.merge(dpto);
		t.commit();
		sesion.close();

	}

	/**
	 * Comprobar que no existe un empleado con mismo nombre y apellidos, si es asi
	 * no hace nada el metodo, si no tiene que comprobar si existe el departamento
	 * 
	 * 
	 * Si el departamento no existe, lo creamos Si el departamento existe y hay
	 * varios con el mismo nombre, los mostramos y pedimos al usuario que elija
	 * entre ellos y se guarda el empleado Usar metodos externos para pedir datos
	 * por ejemplo
	 * 
	 * 
	 * @param nombre
	 * @param ap1
	 * @param ap2
	 * @param dpto
	 */
	private static void anadirEmpleado(String nombre, String ap1, String ap2, String dpto) {
		if (!comprobarExistenciaEmpleado(nombre, ap1, ap2)) {
			Departamentos dptoObjeto = comprobarExistenciaDepartamento(dpto);
			sesion = sf.openSession();
			Transaction t = sesion.beginTransaction();
			Empleados empleadoAnadir = new Empleados(dptoObjeto, nombre, ap1, ap2);
			sesion.persist(empleadoAnadir);
			t.commit();
			if (comprobarExistenciaEmpleado(nombre, ap1, ap2)) {
				System.out.println("Se ha añadido correctamente el empleado");
			} else {
				System.out.println("Error añadiendo el empleado");
			}
			sesion.close();

		} else {
			System.out.println("El empleado ya existe, por eso no se hizo nada");
		}
	}

	private static boolean comprobarExistenciaEmpleado(String nombre, String ap1, String ap2) {
		sesion = sf.openSession();
		String hql = "from Empleados where nombre='" + nombre + "' and apellido1='" + ap1 + "' and apellido2='" + ap2
				+ "'";
		TypedQuery tqDptotq = sesion.createQuery(hql, Departamentos.class);
		ArrayList<String> tqDpto = (ArrayList<String>) tqDptotq.getResultList();
		if (tqDpto.size() == 0) {
			sesion.close();
			return false;
		} else {
			sesion.close();
			return true;
		}

	}

	@SuppressWarnings("unchecked")
	/**
	 * Comprueba si existe el departamento y devuelve el objeto departamento
	 * 
	 * 
	 * 
	 * 
	 * @param dpto
	 * @return
	 */
	private static Departamentos comprobarExistenciaDepartamento(String dpto) {
		Scanner sc = new Scanner(System.in);
		sesion = sf.openSession();
		String elegirLocalidad = "Que localidad quieres elegir\n";
		String hql = "from Departamentos where dnombre='" + dpto + "'";
		TypedQuery<?> tqDptoComprExis = sesion.createQuery(hql, Departamentos.class);
		ArrayList<Departamentos> tqDpto = (ArrayList<Departamentos>) tqDptoComprExis.getResultList();
		ArrayList<Departamentos> localidadesDptos = new ArrayList<Departamentos>();
		for (Departamentos d : tqDpto) {
			if (d.getDnombre().equalsIgnoreCase(dpto)) {
				elegirLocalidad += d.getLoc() + "\n";
				localidadesDptos.add(d);
			}

		}

		if (localidadesDptos.size() > 1) {
			System.out.println(elegirLocalidad);
			String localidad = sc.next();
			for (Departamentos d : localidadesDptos) {
				if (d.getLoc().equalsIgnoreCase(localidad)) {
					return d;
				}

			}

		} else if (localidadesDptos.size() == 1) {
			for (Departamentos d : localidadesDptos) {
				return d;
			}
		} else {
			System.out.println("Elige a que localidad pertenece el departamento");
			String localidad = sc.next();
			anadirDepartamento(dpto, localidad);

		}
		return comprobarExistenciaDepartamento(dpto);

	}

	private static void anadirDepartamento(String nombreDepartamento, String localidad) {
		sesion = sf.openSession();
		Transaction t = sesion.beginTransaction();
		Departamentos dpto = new Departamentos(nombreDepartamento, localidad, null);
		sesion.persist(dpto);
		t.commit();
		sesion.close();
	}

	private static boolean borrarDepartamento(int idDpto) {
		sesion = sf.openSession();
		Transaction t = sesion.beginTransaction();
		Departamentos dptoBorrar = sesion.get(Departamentos.class, idDpto);
		if (JOptionPane.showConfirmDialog(null,
				"Quieres eliminar el departamento " + dptoBorrar.getDnombre() + "?") == JOptionPane.YES_OPTION) {
			sesion.remove(dptoBorrar);
			t.commit();
			sesion.close();
			return true;
		}

		t.rollback();
		sesion.close();
		return false;
	}

	/**
	 * Eliminar un empleado, si esta en un departamento en el que es el unico
	 * empleado, se borra tambien el departamento otro metodo para listar empleados
	 * con nombre de departamento
	 * 
	 */

	private static boolean borrarEmpleado(String nombre, String apellido1, String apellido2) {
		String eleccion = "x";
		sesion = sf.openSession();
		Transaction t = sesion.beginTransaction();
		String hql = "from Empleados where nombre='" + nombre + "'" + " and " + "apellido1='" + apellido1 + "'"
				+ " and " + "apellido2='" + apellido2 + "'";
		TypedQuery<?> tqEmpleados = sesion.createQuery(hql, Empleados.class);
		ArrayList<Empleados> tqEmp = (ArrayList<Empleados>) tqEmpleados.getResultList();
		if (tqEmp.size() == 0) {
			return false;
		}
		Departamentos deptEmpleado = tqEmp.get(0).getDepartamentos();

		// seleccionar todos los empleados que tienen su numero de departamento
		hql = "SELECT count(e) FROM Empleados e WHERE e.departamentos = :departamento";
		Query<Long> qEmp = sesion.createQuery(hql, Long.class);
		qEmp.setParameter("departamento", deptEmpleado);
		Long totalEmpleados = qEmp.getSingleResult();

		if (totalEmpleados == 1) {
			Scanner sc = new Scanner(System.in);

			while (!(eleccion.equals("Y")) && !(eleccion.equals("N"))) {
				System.out.println("¿Quieres borrar también el departamento? Y/N");
				eleccion = sc.next();
			}

		}

		sesion.remove(tqEmp.get(0));

		if (eleccion.equals("Y")) {
			sesion.remove(deptEmpleado);
		}
		t.commit();

		if (!(borrarEmpleado(nombre, apellido1, apellido2))) {
			return true;
		}

		return false;

	}

	private static boolean modificarEmpleado(String nombre, String apellido1, String apellido2,
			String nombreDepartamento) {

		String eleccion = "asdasd";
		StringBuilder departamentoElegidoString = new StringBuilder("Elige la localidad del departamento:\n");
		Departamentos departamentoElegido = new Departamentos();
		sesion = sf.openSession();
		Transaction t = sesion.beginTransaction();
		String hql = "from Empleados where nombre='" + nombre + "'" + " and " + "apellido1='" + apellido1 + "'"
				+ " and " + "apellido2='" + apellido2 + "'";
		TypedQuery<?> tqEmpleados = sesion.createQuery(hql, Empleados.class);
		ArrayList<Empleados> tqEmp = (ArrayList<Empleados>) tqEmpleados.getResultList();
		if (tqEmp.size() == 0) {
			return false;
		}
		Departamentos deptEmpleado = tqEmp.get(0).getDepartamentos();

		hql = "from Departamentos where dnombre ='" + nombreDepartamento + "'";
		TypedQuery<?> tqDept = sesion.createQuery(hql, Departamentos.class);
		ArrayList<Departamentos> listDepartamentos = (ArrayList<Departamentos>) tqDept.getResultList();
		if (listDepartamentos.size() > 1) {
			Scanner sc = new Scanner(System.in);
			for (Departamentos d : listDepartamentos) {
				departamentoElegidoString.append(d.getDnombre() + "(" + d.getLoc() + ")" + "\n");
			}
			while (!(departamentoElegidoString.toString().contains(eleccion))) {
				System.out.println(departamentoElegidoString.toString());
				eleccion = sc.next();

			}
			for (Departamentos d : listDepartamentos) {
				if (d.getLoc() == eleccion && d.getDnombre() == nombreDepartamento) {
					departamentoElegido = d;
				}
			}

		}
		try {
			tqEmp.get(0).setDepartamentos(departamentoElegido);
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}

	}

}
