package xfuzzy;
//++++++++++++++++++++++++++++++++++++++++++++++++++++++//
//     Fuzzy Inference Engine VulnerabilidadDerrame      //
//++++++++++++++++++++++++++++++++++++++++++++++++++++++//

public class VulnerabilidadDerrame implements FuzzyInferenceEngine {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //      Membership function of an input variable       //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private abstract class InnerMembershipFunction {
  double min, max, step;
  abstract double param(int i);
  double center() { return 0; }
  double basis() { return 0; }
  abstract double isEqual(double x);

  double isSmallerOrEqual(double x) {
   double degree=0, mu;
   for(double y=max; y>=x ; y-=step) if((mu = isEqual(y))>degree) degree=mu;
   return degree;
  }

  double isGreaterOrEqual(double x) {
   double degree=0, mu;
   for(double y=min; y<=x ; y+=step) if((mu = isEqual(y))>degree) degree=mu;
   return degree;
  }

  double isEqual(MembershipFunction mf) {
   if(mf instanceof FuzzySingleton)
    { return isEqual( ((FuzzySingleton) mf).getValue()); }
   if((mf instanceof OutputMembershipFunction) &&
      ((OutputMembershipFunction) mf).isDiscrete() ) {
    double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();
    double deg = 0;
    for(int i=0; i<val.length; i++){
     double mu = isEqual(val[i][0]);
     double minmu = (mu<val[i][1] ? mu : val[i][1]);
     if( deg<minmu ) deg = minmu;
    }
    return deg;
   }
   double mu1,mu2,minmu,degree=0;
   for(double x=min; x<=max; x+=step){
    mu1 = mf.compute(x);
    mu2 = isEqual(x);
    minmu = (mu1<mu2 ? mu1 : mu2);
    if( degree<minmu ) degree = minmu;
   }
   return degree;
  }

  double isGreaterOrEqual(MembershipFunction mf) {
   if(mf instanceof FuzzySingleton)
    { return isGreaterOrEqual( ((FuzzySingleton) mf).getValue()); }
   if((mf instanceof OutputMembershipFunction) &&
      ((OutputMembershipFunction) mf).isDiscrete() ) {
    double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();
    double deg = 0;
    for(int i=0; i<val.length; i++){
     double mu = isGreaterOrEqual(val[i][0]);
     double minmu = (mu<val[i][1] ? mu : val[i][1]);
     if( deg<minmu ) deg = minmu;
    }
    return deg;
   }
   double mu1,mu2,minmu,degree=0,greq=0;
   for(double x=min; x<=max; x+=step){
    mu1 = mf.compute(x);
    mu2 = isEqual(x);
    if( mu2>greq ) greq = mu2;
    if( mu1<greq ) minmu = mu1; else minmu = greq;
    if( degree<minmu ) degree = minmu;
   }
   return degree;
  }

  double isSmallerOrEqual(MembershipFunction mf) {
   if(mf instanceof FuzzySingleton)
    { return isSmallerOrEqual( ((FuzzySingleton) mf).getValue()); }
   if((mf instanceof OutputMembershipFunction) &&
      ((OutputMembershipFunction) mf).isDiscrete() ) {
    double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();
    double deg = 0;
    for(int i=0; i<val.length; i++){
     double mu = isSmallerOrEqual(val[i][0]);
     double minmu = (mu<val[i][1] ? mu : val[i][1]);
     if( deg<minmu ) deg = minmu;
    }
    return deg;
   }
   double mu1,mu2,minmu,degree=0,smeq=0;
   for(double x=max; x>=min; x-=step){
    mu1 = mf.compute(x);
    mu2 = isEqual(x);
    if( mu2>smeq ) smeq = mu2;
    if( mu1<smeq ) minmu = mu1; else minmu = smeq;
    if( degree<minmu ) degree = minmu;
   }
   return degree;
  }

  double isGreater(MembershipFunction mf, InnerOperatorset op) {
   if(mf instanceof FuzzySingleton)
    { return op.not(isSmallerOrEqual( ((FuzzySingleton) mf).getValue())); }
   if((mf instanceof OutputMembershipFunction) &&
      ((OutputMembershipFunction) mf).isDiscrete() ) {
    double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();
    double deg = 0;
    for(int i=0; i<val.length; i++){
     double mu = op.not(isSmallerOrEqual(val[i][0]));
     double minmu = (mu<val[i][1] ? mu : val[i][1]);
     if( deg<minmu ) deg = minmu;
    }
    return deg;
   }
   double mu1,mu2,minmu,gr,degree=0,smeq=0;
   for(double x=max; x>=min; x-=step){
    mu1 = mf.compute(x);
    mu2 = isEqual(x);
    if( mu2>smeq ) smeq = mu2;
    gr = op.not(smeq);
    minmu = ( mu1<gr ? mu1 : gr);
    if( degree<minmu ) degree = minmu;
   }
   return degree;
  }

  double isSmaller(MembershipFunction mf, InnerOperatorset op) {
   if(mf instanceof FuzzySingleton)
    { return op.not(isGreaterOrEqual( ((FuzzySingleton) mf).getValue())); }
   if((mf instanceof OutputMembershipFunction) &&
      ((OutputMembershipFunction) mf).isDiscrete() ) {
    double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();
    double deg = 0;
    for(int i=0; i<val.length; i++){
     double mu = op.not(isGreaterOrEqual(val[i][0]));
     double minmu = (mu<val[i][1] ? mu : val[i][1]);
     if( deg<minmu ) deg = minmu;
    }
    return deg;
   }
   double mu1,mu2,minmu,sm,degree=0,greq=0;
   for(double x=min; x<=max; x+=step){
    mu1 = mf.compute(x);
    mu2 = isEqual(x);
    if( mu2>greq ) greq = mu2;
    sm = op.not(greq);
    minmu = ( mu1<sm ? mu1 : sm);
    if( degree<minmu ) degree = minmu;
   }
   return degree;
  }

  double isNotEqual(MembershipFunction mf, InnerOperatorset op) {
   if(mf instanceof FuzzySingleton)
    { return op.not(isEqual( ((FuzzySingleton) mf).getValue())); }
   if((mf instanceof OutputMembershipFunction) &&
      ((OutputMembershipFunction) mf).isDiscrete() ) {
    double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();
    double deg = 0;
    for(int i=0; i<val.length; i++){
     double mu = op.not(isEqual(val[i][0]));
     double minmu = (mu<val[i][1] ? mu : val[i][1]);
     if( deg<minmu ) deg = minmu;
    }
    return deg;
   }
   double mu1,mu2,minmu,degree=0;
   for(double x=min; x<=max; x+=step){
    mu1 = mf.compute(x);
    mu2 = op.not(isEqual(x));
    minmu = (mu1<mu2 ? mu1 : mu2);
    if( degree<minmu ) degree = minmu;
   }
   return degree;
  }

  double isApproxEqual(MembershipFunction mf, InnerOperatorset op) {
   if(mf instanceof FuzzySingleton)
    { return op.moreorless(isEqual( ((FuzzySingleton) mf).getValue())); }
   if((mf instanceof OutputMembershipFunction) &&
      ((OutputMembershipFunction) mf).isDiscrete() ) {
    double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();
    double deg = 0;
    for(int i=0; i<val.length; i++){
     double mu = op.moreorless(isEqual(val[i][0]));
     double minmu = (mu<val[i][1] ? mu : val[i][1]);
     if( deg<minmu ) deg = minmu;
    }
    return deg;
   }
   double mu1,mu2,minmu,degree=0;
   for(double x=min; x<=max; x+=step){
    mu1 = mf.compute(x);
    mu2 = op.moreorless(isEqual(x));
    minmu = (mu1<mu2 ? mu1 : mu2);
    if( degree<minmu ) degree = minmu;
   }
   return degree;
  }

  double isVeryEqual(MembershipFunction mf, InnerOperatorset op) {
   if(mf instanceof FuzzySingleton)
    { return op.very(isEqual( ((FuzzySingleton) mf).getValue())); }
   if((mf instanceof OutputMembershipFunction) &&
      ((OutputMembershipFunction) mf).isDiscrete() ) {
    double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();
    double deg = 0;
    for(int i=0; i<val.length; i++){
     double mu = op.very(isEqual(val[i][0]));
     double minmu = (mu<val[i][1] ? mu : val[i][1]);
     if( deg<minmu ) deg = minmu;
    }
    return deg;
   }
   double mu1,mu2,minmu,degree=0;
   for(double x=min; x<=max; x+=step){
    mu1 = mf.compute(x);
    mu2 = op.very(isEqual(x));
    minmu = (mu1<mu2 ? mu1 : mu2);
    if( degree<minmu ) degree = minmu;
   }
   return degree;
  }

  double isSlightlyEqual(MembershipFunction mf, InnerOperatorset op) {
   if(mf instanceof FuzzySingleton)
    { return op.slightly(isEqual( ((FuzzySingleton) mf).getValue())); }
   if((mf instanceof OutputMembershipFunction) &&
      ((OutputMembershipFunction) mf).isDiscrete() ) {
    double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();
    double deg = 0;
    for(int i=0; i<val.length; i++){
     double mu = op.slightly(isEqual(val[i][0]));
     double minmu = (mu<val[i][1] ? mu : val[i][1]);
     if( deg<minmu ) deg = minmu;
    }
    return deg;
   }
   double mu1,mu2,minmu,degree=0;
   for(double x=min; x<=max; x+=step){
    mu1 = mf.compute(x);
    mu2 = op.slightly(isEqual(x));
    minmu = (mu1<mu2 ? mu1 : mu2);
    if( degree<minmu ) degree = minmu;
   }
   return degree;
  }
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //          Abstract class of an operator set          //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private abstract class InnerOperatorset {
  abstract double and(double a, double b);
  abstract double or(double a, double b);
  abstract double also(double a, double b);
  abstract double imp(double a, double b);
  abstract double not(double a);
  abstract double very(double a);
  abstract double moreorless(double a);
  abstract double slightly(double a);
  abstract double defuz(OutputMembershipFunction mf);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //      Class for the conclusion of a fuzzy rule       //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class InnerConclusion {
  private double degree;
  private InnerMembershipFunction mf;
  private InnerOperatorset op;

  InnerConclusion(double degree, InnerMembershipFunction mf, InnerOperatorset op) {
   this.op = op;
   this.degree = degree;
   this.mf = mf;
  }

  public double degree() { return degree; }
  public double compute(double x) { return op.imp(degree,mf.isEqual(x)); }
  public double center() { return mf.center(); }
  public double basis() { return mf.basis(); }
  public double param(int i) { return mf.param(i); }
  public double min() { return mf.min; }
  public double max() { return mf.max; }
  public double step() { return mf.step; }
  public boolean isSingleton() { 
   return mf.getClass().getName().endsWith("MF_xfl_singleton");
  }
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //      Membership function of an output variable      //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class OutputMembershipFunction implements MembershipFunction {
  public InnerConclusion[] conc;
  public double[] input;
  private InnerOperatorset op;

  OutputMembershipFunction() {
   this.conc = new InnerConclusion[0];
  }

  public void set(int size, InnerOperatorset op, double[] input) {
   this.input = input;
   this.op = op;
   this.conc = new InnerConclusion[size];
  }

  public void set(int pos, double dg, InnerMembershipFunction imf) {
   this.conc[pos] = new InnerConclusion(dg,imf,op);
  }

  public double compute(double x) {
   double dom = conc[0].compute(x);
   for(int i=1; i<conc.length; i++) dom = op.also(dom,conc[i].compute(x));
   return dom;
  }

  public double defuzzify() {
   return op.defuz(this);
  }

  public double min() {
   return conc[0].min();
  }

  public double max() {
   return conc[0].max();
  }

  public double step() {
   return conc[0].step();
  }

  public boolean isDiscrete() {
   for(int i=0; i<conc.length; i++) if(!conc[i].isSingleton()) return false;
   return true;
  }
 
  public double[][] getDiscreteValues() {
   double[][] value = new double[conc.length][2];
   for(int i=0; i<conc.length; i++) {
    value[i][0] = conc[i].param(0);
    value[i][1] = conc[i].degree();
   }
   return value;
  }

 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //      Membership function MF_xfl_bell      //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

  private class MF_xfl_bell extends InnerMembershipFunction {
   double a;
   double b;

   MF_xfl_bell( double min, double max, double step, double param[]){
    super.min = min;
    super.max = max;
    super.step = step;
    this.a = param[0];
    this.b = param[1];
   }
   double param(int _i) {
    switch(_i) {
     case 0: return a;
     case 1: return b;
     default: return 0;
    }
   }
   double isEqual(double x) {
    return Math.exp( -(a-x)*(a-x)/(b*b) ); 
   }
   double isGreaterOrEqual(double x) {
    if(x>a) return 1; return Math.exp( - (x-a)*(x-a)/(b*b) ); 
   }
   double isSmallerOrEqual(double x) {
    if(x<a) return 1; return Math.exp( - (x-a)*(x-a)/(b*b) ); 
   }
   double center() {
    return a; 
   }
   double basis() {
    return b; 
   }
  }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //     Operator set OP_myOP      //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class OP_myOP extends InnerOperatorset {
  double and(double a, double b) {
    return (a<b? a : b); 
  }
  double or(double a, double b) {
    return (a>b? a : b); 
  }
  double also(double a, double b) {
    return (a>b? a : b); 
  }
  double imp(double a, double b) {
    return (a<b? a : b); 
  }
  double not(double a) {
    return 1-a; 
  }
  double very(double a) {
   double w = 2.0;
    return Math.pow(a,w); 
  }
  double moreorless(double a) {
   double w = 0.5;
    return Math.pow(a,w); 
  }
  double slightly(double a) {
    return 4*a*(1-a); 
  }
 double defuz(OutputMembershipFunction mf) {
   double min = mf.min();
   double max = mf.max();
   double step = mf.step();
  double out=min, maximum = 0;
  for(double x=min; x<=max; x+=step) {
   double m = mf.compute(x);
   if(m>=maximum) { maximum = m; out = x; }
  }
  return out;
  }
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //     Operator set OP__default_      //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class OP__default_ extends InnerOperatorset {
  double and(double a, double b) {
    return (a<b? a : b); 
  }
  double or(double a, double b) {
    return (a>b? a : b); 
  }
  double also(double a, double b) {
    return (a>b? a : b); 
  }
  double imp(double a, double b) {
    return (a<b? a : b); 
  }
  double not(double a) {
    return 1-a; 
  }
  double very(double a) {
   double w = 2.0;
    return Math.pow(a,w); 
  }
  double moreorless(double a) {
   double w = 0.5;
    return Math.pow(a,w); 
  }
  double slightly(double a) {
    return 4*a*(1-a); 
  }
 double defuz(OutputMembershipFunction mf) {
   double min = mf.min();
   double max = mf.max();
   double step = mf.step();
  double out = min, maximum = 0;
  int counter = 1;
  for(double x=min; x<=max; x+=step) {
   double m = mf.compute(x);
   if(m == maximum) { out+=x; counter++; }
   if(m > maximum) { maximum = m; out = x; counter = 1; }
  }
  return out/counter;
  }
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeGravIndiceFallec  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeGravIndiceFallec {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 0.00392156862745098;
  double _p_Bajo[] = { 0.0,0.2 };
  double _p_Medio[] = { 0.45,0.2 };
  double _p_Alto[] = { 1.0,0.2 };
  MF_xfl_bell Bajo = new MF_xfl_bell(min,max,step,_p_Bajo);
  MF_xfl_bell Medio = new MF_xfl_bell(min,max,step,_p_Medio);
  MF_xfl_bell Alto = new MF_xfl_bell(min,max,step,_p_Alto);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeGravIndiceLesion  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeGravIndiceLesion {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 0.00392156862745098;
  double _p_Bajo[] = { 0.0,0.2 };
  double _p_Medio[] = { 0.45,0.2 };
  double _p_Alto[] = { 1.0,0.2 };
  MF_xfl_bell Bajo = new MF_xfl_bell(min,max,step,_p_Bajo);
  MF_xfl_bell Medio = new MF_xfl_bell(min,max,step,_p_Medio);
  MF_xfl_bell Alto = new MF_xfl_bell(min,max,step,_p_Alto);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeDInterMayores  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeDInterMayores {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 0.00392156862745098;
  double _p_Bajo[] = { 0.0,0.2 };
  double _p_Medio[] = { 0.45,0.2 };
  double _p_Alto[] = { 1.0,0.2 };
  MF_xfl_bell Bajo = new MF_xfl_bell(min,max,step,_p_Bajo);
  MF_xfl_bell Medio = new MF_xfl_bell(min,max,step,_p_Medio);
  MF_xfl_bell Alto = new MF_xfl_bell(min,max,step,_p_Alto);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeDExterMayores  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeDExterMayores {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 0.00392156862745098;
  double _p_Bajo[] = { 0.0,0.2 };
  double _p_Medio[] = { 0.45,0.2 };
  double _p_Alto[] = { 1.0,0.2 };
  MF_xfl_bell Bajo = new MF_xfl_bell(min,max,step,_p_Bajo);
  MF_xfl_bell Medio = new MF_xfl_bell(min,max,step,_p_Medio);
  MF_xfl_bell Alto = new MF_xfl_bell(min,max,step,_p_Alto);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeDMedioambiental  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeDMedioambiental {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 0.00392156862745098;
  double _p_Bajo[] = { 0.0,0.2 };
  double _p_Medio[] = { 0.45,0.2 };
  double _p_Alto[] = { 1.0,0.2 };
  MF_xfl_bell Bajo = new MF_xfl_bell(min,max,step,_p_Bajo);
  MF_xfl_bell Medio = new MF_xfl_bell(min,max,step,_p_Medio);
  MF_xfl_bell Alto = new MF_xfl_bell(min,max,step,_p_Alto);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeVulnerabilidad  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeVulnerabilidad {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 0.00392156862745098;
  double _p_MuyBaja[] = { 0.0,0.2 };
  double _p_Baja[] = { 0.3,0.2 };
  double _p_Media[] = { 0.5,0.2 };
  double _p_Alta[] = { 0.7,0.2 };
  double _p_MuyAlta[] = { 1.0,0.2 };
  MF_xfl_bell MuyBaja = new MF_xfl_bell(min,max,step,_p_MuyBaja);
  MF_xfl_bell Baja = new MF_xfl_bell(min,max,step,_p_Baja);
  MF_xfl_bell Media = new MF_xfl_bell(min,max,step,_p_Media);
  MF_xfl_bell Alta = new MF_xfl_bell(min,max,step,_p_Alta);
  MF_xfl_bell MuyAlta = new MF_xfl_bell(min,max,step,_p_MuyAlta);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Rulebase RL_Rule_Vulner  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private MembershipFunction[] RL_Rule_Vulner(MembershipFunction inGravIndiceFallec, MembershipFunction inGravIndiceLesion, MembershipFunction inDInterMayores, MembershipFunction inDExterMayores, MembershipFunction inDMedioambiental) {
  double _rl;
  double _input[] = new double[5];
  if(inGravIndiceFallec instanceof FuzzySingleton)
   _input[0] = ((FuzzySingleton) inGravIndiceFallec).getValue();
  if(inGravIndiceLesion instanceof FuzzySingleton)
   _input[1] = ((FuzzySingleton) inGravIndiceLesion).getValue();
  if(inDInterMayores instanceof FuzzySingleton)
   _input[2] = ((FuzzySingleton) inDInterMayores).getValue();
  if(inDExterMayores instanceof FuzzySingleton)
   _input[3] = ((FuzzySingleton) inDExterMayores).getValue();
  if(inDMedioambiental instanceof FuzzySingleton)
   _input[4] = ((FuzzySingleton) inDMedioambiental).getValue();
  OP__default_ _op = new OP__default_();
  OutputMembershipFunction outVulnerabilidad = new OutputMembershipFunction();
  outVulnerabilidad.set(8,_op,_input);
  TP_typeGravIndiceFallec _t_inGravIndiceFallec = new TP_typeGravIndiceFallec();
  TP_typeGravIndiceLesion _t_inGravIndiceLesion = new TP_typeGravIndiceLesion();
  TP_typeDInterMayores _t_inDInterMayores = new TP_typeDInterMayores();
  TP_typeDExterMayores _t_inDExterMayores = new TP_typeDExterMayores();
  TP_typeDMedioambiental _t_inDMedioambiental = new TP_typeDMedioambiental();
  TP_typeVulnerabilidad _t_outVulnerabilidad = new TP_typeVulnerabilidad();
  int _i_outVulnerabilidad=0;
  _rl = _op.and(_t_inGravIndiceFallec.Alto.isEqual(inGravIndiceFallec),_t_inGravIndiceLesion.Alto.isEqual(inGravIndiceLesion));
  outVulnerabilidad.set(_i_outVulnerabilidad,_rl, _t_outVulnerabilidad.MuyAlta); _i_outVulnerabilidad++;
  _rl = _op.and(_op.and(_op.and(_t_inGravIndiceFallec.Alto.isEqual(inGravIndiceFallec),_t_inDInterMayores.Alto.isEqual(inDInterMayores)),_t_inDExterMayores.Alto.isEqual(inDExterMayores)),_t_inDMedioambiental.Alto.isEqual(inDMedioambiental));
  outVulnerabilidad.set(_i_outVulnerabilidad,_rl, _t_outVulnerabilidad.MuyAlta); _i_outVulnerabilidad++;
  _rl = _op.and(_op.and(_t_inGravIndiceFallec.Alto.isEqual(inGravIndiceFallec),_t_inGravIndiceLesion.Medio.isEqual(inGravIndiceLesion)),_t_inDMedioambiental.Medio.isEqual(inDMedioambiental));
  outVulnerabilidad.set(_i_outVulnerabilidad,_rl, _t_outVulnerabilidad.Alta); _i_outVulnerabilidad++;
  _rl = _op.and(_op.and(_t_inGravIndiceFallec.Medio.isEqual(inGravIndiceFallec),_t_inGravIndiceLesion.Alto.isEqual(inGravIndiceLesion)),_t_inDMedioambiental.Alto.isEqual(inDMedioambiental));
  outVulnerabilidad.set(_i_outVulnerabilidad,_rl, _t_outVulnerabilidad.Alta); _i_outVulnerabilidad++;
  _rl = _op.and(_op.and(_t_inGravIndiceFallec.Bajo.isEqual(inGravIndiceFallec),_t_inGravIndiceLesion.Alto.isEqual(inGravIndiceLesion)),_t_inDMedioambiental.Alto.isEqual(inDMedioambiental));
  outVulnerabilidad.set(_i_outVulnerabilidad,_rl, _t_outVulnerabilidad.Media); _i_outVulnerabilidad++;
  _rl = _op.and(_op.and(_t_inGravIndiceFallec.Medio.isEqual(inGravIndiceFallec),_t_inGravIndiceLesion.Alto.isEqual(inGravIndiceLesion)),_t_inDMedioambiental.Medio.isEqual(inDMedioambiental));
  outVulnerabilidad.set(_i_outVulnerabilidad,_rl, _t_outVulnerabilidad.Media); _i_outVulnerabilidad++;
  _rl = _op.and(_op.and(_op.and(_op.and(_t_inGravIndiceFallec.Bajo.isEqual(inGravIndiceFallec),_t_inGravIndiceLesion.Bajo.isEqual(inGravIndiceLesion)),_t_inDInterMayores.Medio.isEqual(inDInterMayores)),_t_inDExterMayores.Medio.isEqual(inDExterMayores)),_t_inDMedioambiental.Bajo.isEqual(inDMedioambiental));
  outVulnerabilidad.set(_i_outVulnerabilidad,_rl, _t_outVulnerabilidad.Baja); _i_outVulnerabilidad++;
  _rl = _op.and(_op.and(_op.and(_op.and(_t_inGravIndiceFallec.Bajo.isEqual(inGravIndiceFallec),_t_inGravIndiceLesion.Bajo.isEqual(inGravIndiceLesion)),_t_inDInterMayores.Bajo.isEqual(inDInterMayores)),_t_inDExterMayores.Bajo.isEqual(inDExterMayores)),_t_inDMedioambiental.Bajo.isEqual(inDMedioambiental));
  outVulnerabilidad.set(_i_outVulnerabilidad,_rl, _t_outVulnerabilidad.MuyBaja); _i_outVulnerabilidad++;
  MembershipFunction[] _output = new MembershipFunction[1];
  _output[0] = new FuzzySingleton(outVulnerabilidad.defuzzify());
  return _output;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //               Fuzzy Inference Engine                //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public double[] crispInference(double[] _input) {
  MembershipFunction inGravIndiceFallec = new FuzzySingleton(_input[0]);
  MembershipFunction inGravIndiceLesion = new FuzzySingleton(_input[1]);
  MembershipFunction inDInterMayores = new FuzzySingleton(_input[2]);
  MembershipFunction inDExterMayores = new FuzzySingleton(_input[3]);
  MembershipFunction inDMedioambiental = new FuzzySingleton(_input[4]);
  MembershipFunction outVulnerabilidad;
  MembershipFunction[] _call;
  _call = RL_Rule_Vulner(inGravIndiceFallec,inGravIndiceLesion,inDInterMayores,inDExterMayores,inDMedioambiental); outVulnerabilidad=_call[0];
  double _output[] = new double[1];
  if(outVulnerabilidad instanceof FuzzySingleton)
   _output[0] = ((FuzzySingleton) outVulnerabilidad).getValue();
  else _output[0] = ((OutputMembershipFunction) outVulnerabilidad).defuzzify();
  return _output;
 }

 public double[] crispInference(MembershipFunction[] _input) {
  MembershipFunction inGravIndiceFallec = _input[0];
  MembershipFunction inGravIndiceLesion = _input[1];
  MembershipFunction inDInterMayores = _input[2];
  MembershipFunction inDExterMayores = _input[3];
  MembershipFunction inDMedioambiental = _input[4];
  MembershipFunction outVulnerabilidad;
  MembershipFunction[] _call;
  _call = RL_Rule_Vulner(inGravIndiceFallec,inGravIndiceLesion,inDInterMayores,inDExterMayores,inDMedioambiental); outVulnerabilidad=_call[0];
  double _output[] = new double[1];
  if(outVulnerabilidad instanceof FuzzySingleton)
   _output[0] = ((FuzzySingleton) outVulnerabilidad).getValue();
  else _output[0] = ((OutputMembershipFunction) outVulnerabilidad).defuzzify();
  return _output;
 }

 public MembershipFunction[] fuzzyInference(double[] _input) {
  MembershipFunction inGravIndiceFallec = new FuzzySingleton(_input[0]);
  MembershipFunction inGravIndiceLesion = new FuzzySingleton(_input[1]);
  MembershipFunction inDInterMayores = new FuzzySingleton(_input[2]);
  MembershipFunction inDExterMayores = new FuzzySingleton(_input[3]);
  MembershipFunction inDMedioambiental = new FuzzySingleton(_input[4]);
  MembershipFunction outVulnerabilidad;
  MembershipFunction[] _call;
  _call = RL_Rule_Vulner(inGravIndiceFallec,inGravIndiceLesion,inDInterMayores,inDExterMayores,inDMedioambiental); outVulnerabilidad=_call[0];
  MembershipFunction _output[] = new MembershipFunction[1];
  _output[0] = outVulnerabilidad;
  return _output;
 }

 public MembershipFunction[] fuzzyInference(MembershipFunction[] _input) {
  MembershipFunction inGravIndiceFallec = _input[0];
  MembershipFunction inGravIndiceLesion = _input[1];
  MembershipFunction inDInterMayores = _input[2];
  MembershipFunction inDExterMayores = _input[3];
  MembershipFunction inDMedioambiental = _input[4];
  MembershipFunction outVulnerabilidad;
  MembershipFunction[] _call;
  _call = RL_Rule_Vulner(inGravIndiceFallec,inGravIndiceLesion,inDInterMayores,inDExterMayores,inDMedioambiental); outVulnerabilidad=_call[0];
  MembershipFunction _output[] = new MembershipFunction[1];
  _output[0] = outVulnerabilidad;
  return _output;
 }

}
