#include <time.h>     
#include <iostream>     
#include <algorithm>    
#include <vector>
#include <string>

std::string  fragName(int i)
{
  std::string ret;
  
  switch(i) {    
  case(1): ret = ":A1"; break;
  case(2): ret = ":A2"; break; 
  case(3): ret = ":A3"; break;
  case(4): ret = ":A4"; break;
  case(5): ret = ":A5"; break;
  case(6): ret = ":A6"; break;
  case(7): ret = ":B1"; break;
  case(8): ret = ":B2"; break;
  case(9): ret = ":B3"; break;
  case(10): ret = ":B4"; break;
  case(11): ret = ":B5"; break;
  case(12): ret = ":C1"; break;
  case(13): ret = ":C2"; break;
  case(14): ret = ":C3"; break;
  case(15): ret = ":C4"; break;
  }

  return ret;
}


int main () 
{
  int n, r, goodCommutation, goodTotal;
  n = 15;
  r = 10;    

  int maxCommutation=0;  
  int frequenze[3004];

  int contatore = 0;

  //int myints[] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
  int myints[] = {4,14,9,15,6,8,10,5,12,2,13,7,1,11,3};
  int bestints[n];
  std::vector<bool> v(n);
  //std::sort (myints,myints+n);
  //std::srand ( unsigned ( std::time(0) ) );
  //std::random_shuffle (myints,myints+n);


  bool transition[16][16];
  for (int i = 1; i < 16; ++i) 
    for (int j = 1; j < 16; ++j) 
      { transition[i][j] = false; }
  for (int j = 1; j < 16; ++j) 
    { transition[0][j] = true; }

  transition[1][9] = transition[1][10] = transition[1][11] = transition[1][12] = transition[1][13] = transition[1][14] = transition[1][15] = true;
  transition[2][7] = transition[2][8] = transition[2][9] = transition[2][10] = transition[2][11] = transition[2][13] = transition[2][15] = true;
  transition[3][7] = transition[3][8] = transition[3][9] = transition[3][10] = transition[3][11] = transition[3][12] = transition[3][13] = transition[3][14] = transition[3][15] = true;
  transition[4][7] = transition[4][8] = transition[4][9] = transition[4][12] = transition[4][13] = transition[4][14] = transition[4][15] = true;
  transition[5][9] = transition[5][10] = transition[5][11] = transition[5][12] = transition[5][13] = transition[5][14] = transition[5][15] = true;
  transition[6][7] = transition[6][8] = transition[6][9] = transition[6][10] = transition[6][11] = transition[6][13] = transition[6][15] = true;

  transition[7][1] = transition[7][2] = transition[7][3] = transition[7][4] = transition[7][6] = transition[7][12] = transition[7][13] = transition[7][14] = transition[7][15] = true;
  transition[8][1] = transition[8][2] = transition[8][3] = transition[8][4] = transition[7][6] = transition[8][12] = transition[8][13] = transition[8][14] = transition[8][15] = true;
  transition[9][2] = transition[9][3] = transition[9][5] = transition[9][6] = transition[9][12] = transition[9][13] = transition[9][14] = transition[9][15] = true;
  transition[10][1] = transition[10][2] = transition[10][3] = transition[10][5] = transition[10][12] = transition[10][13] = transition[10][14] = transition[10][15] = true;
  transition[11][1] = transition[11][2] = transition[11][3] = transition[11][5] = transition[11][6] = transition[11][12] = transition[11][13] = transition[11][14] = transition[11][15] = true;

  transition[12][1] = transition[12][2] = transition[12][3] = transition[12][5] = transition[12][6] = transition[12][7] = transition[12][8] = transition[12][9] = true;
  transition[13][1] = transition[13][3] = transition[13][4] = transition[13][5] = transition[13][6] = transition[13][7] = transition[13][8] = transition[13][9] = transition[13][10] = transition[13][11] = true;
  transition[14][2] = transition[14][3] = transition[14][5] = transition[14][6] = transition[14][9] = transition[14][10] = transition[14][11] = true;
  transition[15][1] = transition[15][2] = transition[15][4] = transition[15][5] = transition[15][6] = transition[15][7] = transition[15][8] = transition[15][10] = transition[15][11] = true;


  clock_t startTime = clock();
  goodCommutation = 0;

  std::fill(v.begin() + n - r, v.end(), true);
   
  do 
    {
      bool good = true;
      int oldPosition = 0;
      for (int i = 0;  good && i < n; ++i) 
	{
	  if (v[i]) 
	    {
	      std::cout << fragName(myints[i]) << " ";
	      good = transition[oldPosition][myints[i]];
	      oldPosition = myints[i];
	    }	      
	}

      if (good)  
	{
	goodCommutation++;
	std::cout << " ok";	  
	}
      else
	{   
	  std::cout << " nada";	  
	}
      std::cout << std::endl;

    } while (std::next_permutation(v.begin(), v.end()));

  //std::cout <<"Commutazioni buone >>" << goodCommutation << "<<" <<'\n';

  frequenze[goodCommutation] += 1;

  std::cout <<'\n' << "Sequenze buone totali <<" << goodCommutation << ">>" <<'\n';
      
  std::cout << double( clock() - startTime ) / (double)CLOCKS_PER_SEC<< " seconds." << std::endl;

      
  return 0;
}

