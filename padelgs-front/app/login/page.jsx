import FormLogin from "../components/FormLogin";
import TitleSections from "../components/TitleSections";

const Page = () => {
  return (
    <>
      <TitleSections
        title={"Iniciar Sesion"}
        subtitle={"Rappa no podes hackear esto"}
      />
      <div className="mb-20">
        <FormLogin />
      </div>
    </>
  );
};

export default Page;
