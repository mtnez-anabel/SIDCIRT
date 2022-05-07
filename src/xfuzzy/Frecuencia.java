package xfuzzy;

//++++++++++++++++++++++++++++++++++++++++++++++++++++++//
//     Fuzzy Inference Engine Frecuencia      //
//++++++++++++++++++++++++++++++++++++++++++++++++++++++//

public class Frecuencia implements FuzzyInferenceEngine {

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
 //      Membership function MF_xfl_sigma      //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

  private class MF_xfl_sigma extends InnerMembershipFunction {
   double a;
   double b;

   MF_xfl_sigma( double min, double max, double step, double param[]){
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
    return 1/(1+ Math.exp( (a-x)/b )); 
   }
   double isGreaterOrEqual(double x) {
      double y = (b>0? x : min);
      return 1 / (1+ Math.exp( (a-y)/b ));
   }
   double isSmallerOrEqual(double x) {
      double y = (b<0? x : max);
      return 1 / (1+ Math.exp( (a-y)/b ));
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
  double out=min, maximum = 0;
  for(double x=min; x<=max; x+=step) {
   double m = mf.compute(x);
   if(m>=maximum) { maximum = m; out = x; }
  }
  return out;
  }
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeinFrec  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeinFrec {
  private double min = 0.0;
  private double max = 150.0;
  private double step = 0.5882352941176471;
  double _p_Frecuente[] = { 1.0,-0.1 };
  double _p_Posible[] = { 8.0,3.0 };
  double _p_Raro[] = { 30.0,10.0 };
  double _p_ExtrmadamenteRaro[] = { 80.0,25.0 };
  double _p_Improbable[] = { 115.0,3.0 };
  MF_xfl_sigma Frecuente = new MF_xfl_sigma(min,max,step,_p_Frecuente);
  MF_xfl_bell Posible = new MF_xfl_bell(min,max,step,_p_Posible);
  MF_xfl_bell Raro = new MF_xfl_bell(min,max,step,_p_Raro);
  MF_xfl_bell ExtrmadamenteRaro = new MF_xfl_bell(min,max,step,_p_ExtrmadamenteRaro);
  MF_xfl_sigma Improbable = new MF_xfl_sigma(min,max,step,_p_Improbable);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeoutFrec  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeoutFrec {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 0.00392156862745098;
  double _p_Improbable[] = { 0.15,-0.015 };
  double _p_ExtremadamenteRaro[] = { 0.3,0.1 };
  double _p_Raro[] = { 0.5,0.1 };
  double _p_Posible[] = { 0.7,0.1 };
  double _p_Frecuente[] = { 1.0,0.15 };
  MF_xfl_sigma Improbable = new MF_xfl_sigma(min,max,step,_p_Improbable);
  MF_xfl_bell ExtremadamenteRaro = new MF_xfl_bell(min,max,step,_p_ExtremadamenteRaro);
  MF_xfl_bell Raro = new MF_xfl_bell(min,max,step,_p_Raro);
  MF_xfl_bell Posible = new MF_xfl_bell(min,max,step,_p_Posible);
  MF_xfl_bell Frecuente = new MF_xfl_bell(min,max,step,_p_Frecuente);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Rulebase RL_Rule_Frec  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private MembershipFunction[] RL_Rule_Frec(MembershipFunction inFrec) {
  double _rl;
  double _input[] = new double[1];
  if(inFrec instanceof FuzzySingleton)
   _input[0] = ((FuzzySingleton) inFrec).getValue();
  OP__default_ _op = new OP__default_();
  OutputMembershipFunction outFrec = new OutputMembershipFunction();
  outFrec.set(5,_op,_input);
  TP_typeinFrec _t_inFrec = new TP_typeinFrec();
  TP_typeoutFrec _t_outFrec = new TP_typeoutFrec();
  int _i_outFrec=0;
  _rl = _t_inFrec.Frecuente.isEqual(inFrec);
  outFrec.set(_i_outFrec,_rl, _t_outFrec.Frecuente); _i_outFrec++;
  _rl = _t_inFrec.Posible.isEqual(inFrec);
  outFrec.set(_i_outFrec,_rl, _t_outFrec.Posible); _i_outFrec++;
  _rl = _t_inFrec.Raro.isEqual(inFrec);
  outFrec.set(_i_outFrec,_rl, _t_outFrec.Raro); _i_outFrec++;
  _rl = _t_inFrec.ExtrmadamenteRaro.isEqual(inFrec);
  outFrec.set(_i_outFrec,_rl, _t_outFrec.ExtremadamenteRaro); _i_outFrec++;
  _rl = _t_inFrec.Improbable.isEqual(inFrec);
  outFrec.set(_i_outFrec,_rl, _t_outFrec.Improbable); _i_outFrec++;
  MembershipFunction[] _output = new MembershipFunction[1];
  _output[0] = new FuzzySingleton(outFrec.defuzzify());
  return _output;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //               Fuzzy Inference Engine                //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public double[] crispInference(double[] _input) {
  MembershipFunction inFrec = new FuzzySingleton(_input[0]);
  MembershipFunction outFrec;
  MembershipFunction[] _call;
  _call = RL_Rule_Frec(inFrec); outFrec=_call[0];
  double _output[] = new double[1];
  if(outFrec instanceof FuzzySingleton)
   _output[0] = ((FuzzySingleton) outFrec).getValue();
  else _output[0] = ((OutputMembershipFunction) outFrec).defuzzify();
  return _output;
 }

 public double[] crispInference(MembershipFunction[] _input) {
  MembershipFunction inFrec = _input[0];
  MembershipFunction outFrec;
  MembershipFunction[] _call;
  _call = RL_Rule_Frec(inFrec); outFrec=_call[0];
  double _output[] = new double[1];
  if(outFrec instanceof FuzzySingleton)
   _output[0] = ((FuzzySingleton) outFrec).getValue();
  else _output[0] = ((OutputMembershipFunction) outFrec).defuzzify();
  return _output;
 }

 public MembershipFunction[] fuzzyInference(double[] _input) {
  MembershipFunction inFrec = new FuzzySingleton(_input[0]);
  MembershipFunction outFrec;
  MembershipFunction[] _call;
  _call = RL_Rule_Frec(inFrec); outFrec=_call[0];
  MembershipFunction _output[] = new MembershipFunction[1];
  _output[0] = outFrec;
  return _output;
 }

 public MembershipFunction[] fuzzyInference(MembershipFunction[] _input) {
  MembershipFunction inFrec = _input[0];
  MembershipFunction outFrec;
  MembershipFunction[] _call;
  _call = RL_Rule_Frec(inFrec); outFrec=_call[0];
  MembershipFunction _output[] = new MembershipFunction[1];
  _output[0] = outFrec;
  return _output;
 }

}
